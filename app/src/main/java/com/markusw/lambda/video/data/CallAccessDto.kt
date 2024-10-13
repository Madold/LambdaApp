package com.markusw.lambda.video.data

import com.markusw.lambda.core.domain.model.User

data class CallAccessDto(
    val roomId: String = "",
    val user: User = User(),
    val accessState: String = "waiting"
)
