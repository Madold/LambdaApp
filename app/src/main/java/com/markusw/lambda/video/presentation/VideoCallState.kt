package com.markusw.lambda.video.presentation

import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.video.data.WaitingConfirmation
import io.getstream.video.android.core.Call

data class VideoCallState(
    val call: Call? = null,
    val callStatus: CallStatus? = CallStatus.WaitingForApproval,
    val chatConnectionStatus: ChatConnectionStatus = ChatConnectionStatus.Connecting,
    val waitingConfirmations: List<WaitingConfirmation> = emptyList(),
    val roomId: String? = null,
    val authorId: String? = null,
    val loggedUser: User = User(),
    val isLoadingVideoCall: Boolean = false,
    val chatChannelId: String? = null
)
