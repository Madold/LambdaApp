package com.markusw.lambda.video.presentation

import com.markusw.lambda.video.data.CallAccessDto

sealed interface VideoCallEvent {
    data object Disconnect: VideoCallEvent
    data object JoinCall: VideoCallEvent
    data object FinishSession: VideoCallEvent
    data class AcceptCall(val dto: CallAccessDto): VideoCallEvent
    data class RejectCall(val dto: CallAccessDto): VideoCallEvent
    data object StartCall: VideoCallEvent
}