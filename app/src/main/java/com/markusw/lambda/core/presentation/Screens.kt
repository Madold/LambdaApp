package com.markusw.lambda.core.presentation

sealed class Screens(val route: String) {

    data object Login: Screens("login")

    data object Video: Screens("video")

}