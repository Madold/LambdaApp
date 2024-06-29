package com.markusw.lambda.auth.presentation

sealed interface AuthViewModelEvent {
    data object VideoClientInitialized: AuthViewModelEvent
}