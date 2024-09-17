package com.markusw.lambda.core.data.model

data class MentoringDto(
    //PRIMARY KEY
    val roomId: String = "",
    val price: Long = 0,
    val requesterId: String = "",
    val requesterDescription: String = "",
    val authorId: String? = null,
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val topic: String = ""
)
