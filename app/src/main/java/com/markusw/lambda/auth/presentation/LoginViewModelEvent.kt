package com.markusw.lambda.auth.presentation

sealed interface LoginViewModelEvent {

    data object AuthSuccessful: LoginViewModelEvent
    data class AuthFailed(val errorMessage: String): LoginViewModelEvent

}