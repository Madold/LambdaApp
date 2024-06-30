package com.markusw.lambda.core.domain.model

data class Mentoring(
    val roomId: String = "",
    val participants: List<User> = emptyList(),
    val totalRevenue: Long = 0,
    val price: Long = 0,
    val requester: User = User(),
    val requesterDescription: String = "",
    val author: User? = null,
    val title: String = "",
    val description: String = "",
    val coverUrl: String = ""
)