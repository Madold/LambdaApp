package com.markusw.lambda.video.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.lambda.core.domain.VideoClient
import com.markusw.lambda.core.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject constructor(
    private val videoClient: VideoClient,
    savedStateHandle: SavedStateHandle
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
        videoClient.callToRoom(roomId)

        _state.update {
            it.copy(
                call = videoClient.getCall()
            )
        }
    }

    fun onEvent(event: VideoCallEvent) {
        when (event) {
            is VideoCallEvent.Disconnect -> {
                videoClient.disconnect()
                _state.update {
                    it.copy(
                        callStatus = CallStatus.Ended
                    )
                }
            }

            is VideoCallEvent.JoinCall -> {
                joinCall()
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
                            callStatus = CallStatus.Ended
                        )
                    }
                    Log.d(TAG, "Call error ${result.message}")
                }
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            call = result.data,
                            callStatus = CallStatus.Running
                        )
                    }
                    Log.d(TAG, "Call success")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        channel.close()
    }

}