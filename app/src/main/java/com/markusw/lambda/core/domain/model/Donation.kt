package com.markusw.lambda.core.domain.model

data class Donation(
    val author: User = User(),
    val mentoring: Mentoring = Mentoring(),
    val amount: Long = 0
)
