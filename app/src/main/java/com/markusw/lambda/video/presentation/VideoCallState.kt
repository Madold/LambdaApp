package com.markusw.lambda.video.presentation

import io.getstream.video.android.core.Call

data class VideoCallState(
    val call: Call? = null,
    val callStatus: CallStatus? = null
)
