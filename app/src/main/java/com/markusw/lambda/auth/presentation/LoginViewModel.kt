package com.markusw.lambda.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.markusw.lambda.auth.domain.repository.AuthRepository
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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()
    private val channel = Channel<LoginViewModelEvent>()
    val events = channel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnGoogleSignInResult -> handleGoogleSignIn(event.credential)
            is LoginEvent.StartGoogleSignIn -> {
                _state.update {
                    it.copy(isLoading = true)
                }
            }
            is LoginEvent.FinishGoogleSignIn -> {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleGoogleSignIn(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = authRepository.authenticateWithCredential(credential)) {
                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    channel.send(
                        LoginViewModelEvent.AuthFailed(
                            result.message ?: "Unknown error"
                        )
                    )
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    channel.send(LoginViewModelEvent.AuthSuccessful)
                }
            }
        }
    }

}