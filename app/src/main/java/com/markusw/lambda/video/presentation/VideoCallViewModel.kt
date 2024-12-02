package com.markusw.lambda.video.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.lambda.auth.domain.AuthService
import com.markusw.lambda.core.domain.ChatClient
import com.markusw.lambda.core.domain.VideoClient
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject constructor(
    private val remoteDatabase: RemoteDatabase,
    private val videoClient: VideoClient,
    private val chatClient: ChatClient,
    authService: AuthService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(VideoCallState())
    val state = _state.asStateFlow()
    private val channel = Channel<VideoCallViewModelEvent>()
    val events = channel.receiveAsFlow()

    companion object {
        const val TAG = "VideoCallViewModel"
    }

    init {
        val roomId = savedStateHandle.get<String>("roomId") ?: "main-room"
        val authorId = savedStateHandle.get<String>("authorId") ?: "1234"
        val loggedUser = authService.getLoggedUser()

        videoClient.callToRoom(roomId)

        _state.update {
            it.copy(
                callStatus = if (authorId == loggedUser?.id) CallStatus.Idle else CallStatus.WaitingForApproval
            )
        }

        viewModelScope.launch {
            delay(1200)
            val isSuccessfully = chatClient.initChatClient(
                username = loggedUser?.displayName ?: "Anonymous",
                userId = loggedUser?.id ?: "1234"
            )

            if (isSuccessfully) {
                _state.update {
                    it.copy(
                        chatConnectionStatus = ChatConnectionStatus.Connected
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        chatConnectionStatus = ChatConnectionStatus.Error
                    )
                }
            }

        }

        //Flow for everyone
        viewModelScope.launch {
            remoteDatabase
                .getCallStateById(roomId)
                .collectLatest { callState ->
                    if (callState == "finished") {
                        _state.update { it.copy(callStatus = CallStatus.Finished) }

                        // This ensures that the participants are also disconnected from the call and not just the host.
                        if (authorId != loggedUser?.id) {
                            videoClient.disconnect()
                        }
                    }

                    if (callState == "running") {
                        joinCall()
                    }

                }
        }

        //Flow only for participants
        if (authorId != loggedUser?.id) {
            viewModelScope.launch {
                remoteDatabase
                    .getCallAccess(roomId, loggedUser?.id ?: "1234")
                    .collectLatest { acesss ->
                        if (acesss == "granted") {
                            _state.update {
                                it.copy(
                                    callStatus = CallStatus.AccessGranted
                                )
                            }
                        }
                    }

            }
        }

        //Flow only for host
        if (authorId == loggedUser?.id) {
            viewModelScope.launch {
                remoteDatabase
                    .getWaitingConfirmations(roomId)
                    .collectLatest { confirmations ->
                        _state.update {
                            it.copy(
                                waitingConfirmations = confirmations
                            )
                        }
                    }
            }
        }


        _state.update {
            it.copy(
                call = videoClient.getCall(),
                roomId = roomId,
                authorId = authorId,
                loggedUser = loggedUser ?: User()
            )
        }
    }

    fun onEvent(event: VideoCallEvent) {
        when (event) {
            is VideoCallEvent.Disconnect -> {
                videoClient.disconnect()
                _state.update {
                    it.copy(
                        callStatus = CallStatus.Leaved
                    )
                }
            }

            is VideoCallEvent.JoinCall -> {
                joinCall()
            }

            VideoCallEvent.FinishSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val result = videoClient.finishCall()) {
                        is Result.Error -> {

                        }

                        is Result.Success -> {

                            val emails = remoteDatabase.getUserEmailsFromRoom(state.value.roomId ?: "1234")

                            //remoteDatabase.finishCall(state.value.roomId ?: "1234")
                            //remoteDatabase.deleteMentoringById(state.value.roomId ?: "1234")
                        }
                    }
                }
            }

            is VideoCallEvent.AcceptCall -> {
                viewModelScope.launch(Dispatchers.IO) {
                    remoteDatabase.acceptCall(event.dto)
                }
            }

            is VideoCallEvent.RejectCall -> {
                viewModelScope.launch {
                    remoteDatabase.rejectCall(event.dto)
                }
            }

            is VideoCallEvent.StartCall -> {
                viewModelScope.launch {
                    remoteDatabase.startCall(state.value.roomId ?: "1234")
                }
            }

            is VideoCallEvent.SetChatChannelId -> {
                _state.update {
                    it.copy(
                        chatChannelId = event.channelId
                    )
                }
            }
        }
    }

    private fun joinCall() {
        if (state.value.callStatus == CallStatus.Running) return

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = videoClient.joinCall()) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            callStatus = CallStatus.Error
                        )
                    }
                    videoClient.disconnect()
                    Log.d(TAG, "Call error ${result.message}")
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(
                            call = result.data,
                            callStatus = CallStatus.Running
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        channel.close()
    }

}