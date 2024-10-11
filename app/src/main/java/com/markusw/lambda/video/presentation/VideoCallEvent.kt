package com.markusw.lambda.video.presentation

sealed interface VideoCallEvent {
    data object Disconnect: VideoCallEvent
    data object JoinCall: VideoCallEvent
    data object FinishSession: VideoCallEvent
}