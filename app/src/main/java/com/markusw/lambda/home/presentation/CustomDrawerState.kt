package com.markusw.lambda.home.presentation

enum class CustomDrawerState {
    Opened,
    Closed
}

fun CustomDrawerState.isOpened() = this.name == "Opened"

fun CustomDrawerState.opposite(): CustomDrawerState {
    return if (this == CustomDrawerState.Opened) CustomDrawerState.Closed
    else CustomDrawerState.Opened
}