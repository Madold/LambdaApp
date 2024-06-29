package com.markusw.lambda.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.markusw.lambda.auth.domain.repository.AuthRepository
import com.markusw.lambda.core.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val channel = Channel<LoginViewModelEvent>()
    val events = channel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnGoogleSignInResult -> handleGoogleSignIn(event.credential)
        }
    }

    private fun handleGoogleSignIn(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = authRepository.authenticateWithCredential(credential)) {
                is Result.Error -> {
                    channel.send(
                        LoginViewModelEvent.AuthFailed(
                            result.message ?: "Unknown error"
                        )
                    )
                }

                is Result.Success -> {
                    channel.send(LoginViewModelEvent.AuthSuccessful)
                }
            }
        }
    }

}