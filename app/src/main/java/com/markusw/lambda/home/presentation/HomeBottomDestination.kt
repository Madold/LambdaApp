package com.markusw.lambda.home.presentation

import com.markusw.lambda.R

sealed class HomeBottomDestination(
    val route: String,
    val icon: Int,
    val label: String
) {

    companion object {
        fun entries() = listOf(
            LiveTutorials,
            TutoringRequests
        )
    }

    data object LiveTutorials: HomeBottomDestination(
        route = "live_tutorials",
        icon = R.drawable.ic_video_call,
        label = "Clases en vivo"
    )

    data object TutoringRequests: HomeBottomDestination(
        route = "tutoring_requests",
        icon = R.drawable.ic_graduation_cap,
        label = "Solicitudes de tutorías"
    )

}