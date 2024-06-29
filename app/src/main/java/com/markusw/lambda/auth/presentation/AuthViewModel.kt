package com.markusw.lambda.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.lambda.core.domain.VideoClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val videoClient: VideoClient
) : ViewModel() {

    private val _state = MutableStateFlow(AuthScreenState())
    val state = _state.asStateFlow()
    private val channel = Channel<AuthViewModelEvent>()
    val events = channel.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.ChangeUserName -> {
                _state.update {
                    it.copy(
                        userName = event.userName
                    )
                }
            }
            is AuthEvent.JoinSession -> {
                videoClient.initVideoClient(state.value.userName)
                viewModelScope.launch { channel.send(AuthViewModelEvent.VideoClientInitialized) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        channel.close()
    }

}