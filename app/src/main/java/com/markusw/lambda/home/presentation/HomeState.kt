package com.markusw.lambda.home.presentation

import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User

data class HomeState(
    val users: List<User> = emptyList(),
    val tutorials: List<Mentoring> = emptyList(),
    val isRequestTutoringDialogVisible: Boolean = false,
    val mentoringTitle: String = "",
    val mentoringDescription: String = "",
    val mentoringRequesterDescription: String = "",
    val mentoringCoverUri: String = "",
    val mentoringPrice: Long = 0,
    val donationAmount: Long = 0,
    val isSavingMentoring: Boolean = false,
    val selectedMentoring: Mentoring? = null,
    val isProvideMentoringDialogVisible: Boolean = false,
    val isStartingLiveMentoring: Boolean = false
)