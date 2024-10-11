package com.markusw.lambda.home.presentation

sealed interface HomeViewModelEvent {

    data class VideoClientInitialized(val roomId: String, val authorId: String): HomeViewModelEvent
    data object LogoutSuccess: HomeViewModelEvent

}