package com.markusw.lambda.home.presentation

import com.markusw.lambda.core.domain.model.User

data class HomeState(
    val users: List<User> = emptyList()
)