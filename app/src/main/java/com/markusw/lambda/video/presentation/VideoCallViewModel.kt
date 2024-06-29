package com.markusw.lambda.video.presentation

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
    private val videoClient: VideoClient
) : ViewModel() {

    private val _state = MutableStateFlow(VideoCallState())
    val state = _state.asStateFlow()
    private val channel = Channel<VideoCallViewModelEvent>()
    val events = channel.receiveAsFlow()

    init {
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
        viewModelScope.launch {
            when (val result = videoClient.joinCall()) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            callStatus = CallStatus.Ended
                        )
                    }
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