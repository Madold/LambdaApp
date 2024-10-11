package com.markusw.lambda.video.presentation

import com.markusw.lambda.core.domain.model.User
import io.getstream.video.android.core.Call

data class VideoCallState(
    val call: Call? = null,
    val callStatus: CallStatus? = null,
    val roomId: String? = null,
    val authorId: String? = null,
    val loggedUser: User = User()
)
