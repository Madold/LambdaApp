package com.markusw.lambda.auth.presentation

import com.google.firebase.auth.AuthCredential

sealed interface LoginEvent {

    data class OnGoogleSignInResult(val credential: AuthCredential): LoginEvent
    data object StartGoogleSignIn: LoginEvent
    data object FinishGoogleSignIn: LoginEvent

}