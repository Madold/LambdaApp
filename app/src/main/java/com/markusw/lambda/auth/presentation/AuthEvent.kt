package com.markusw.lambda.auth.presentation

sealed interface AuthEvent {
    data class ChangeUserName(val userName: String): AuthEvent
    data object JoinSession: AuthEvent
}