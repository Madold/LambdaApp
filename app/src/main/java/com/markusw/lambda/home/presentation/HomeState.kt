package com.markusw.lambda.home.presentation

import com.markusw.lambda.core.domain.model.User

data class HomeState(
    val users: List<User> = emptyList(),
    val isRequestTutoringDialogVisible: Boolean = false,
    val mentoringTitle: String = "",
    val mentoringDescription: String = "",
    val mentoringCoverUrl: String = "",
    val mentoringPrice: Long = 0,
    val donationAmount: Long = 0
)