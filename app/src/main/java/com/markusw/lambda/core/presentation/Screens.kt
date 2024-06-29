package com.markusw.lambda.core.presentation

sealed class Screens(val route: String) {

    data object Auth: Screens("auth")

    data object Video: Screens("video")

}