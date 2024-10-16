package com.markusw.lambda.home.presentation

import com.markusw.lambda.core.domain.model.Mentoring

sealed interface HomeEvent {
    data class ChangeRequestTutoringDialogVisibility(val isVisible: Boolean): HomeEvent
    data class ChangeMentoringTitle(val title: String): HomeEvent
    data class ChangeMentoringDescription(val description: String): HomeEvent
    data class ChangeMentoringPrice(val price: Long): HomeEvent
    data class ChangeDonationAmount(val amount: Long): HomeEvent
    data class ChangeMentoringCoverUrl(val coverUrl: String): HomeEvent
    data object CreateMentoringRequest: HomeEvent
    data class ChangeMentoringRequesterDescription(val description: String): HomeEvent
    data object BringLiveMentoring: HomeEvent
    data class  ChangeSelectedMentoring(val mentoring: Mentoring): HomeEvent
    data class ChangeProvideMentoringDialogVisibility(val isVisible: Boolean): HomeEvent
    data class DonateToMentoring(val mentoring: Mentoring, val amount: Long): HomeEvent
    data class PayMentoringAccess(val mentoring: Mentoring): HomeEvent
    data class JoinLiveMentoring(val mentoringId: String, val authorId: String): HomeEvent
    data class ChangeSelectedTopicFilter(val topic: TopicFilter) : HomeEvent
    data class ChangeMentoringTopic(val label: String) : HomeEvent
    data object Logout: HomeEvent
}