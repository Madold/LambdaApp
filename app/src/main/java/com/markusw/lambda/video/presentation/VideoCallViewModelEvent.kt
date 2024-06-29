package com.markusw.lambda.video.presentation

sealed interface VideoCallViewModelEvent {
    data object CallLeaved: VideoCallViewModelEvent
}