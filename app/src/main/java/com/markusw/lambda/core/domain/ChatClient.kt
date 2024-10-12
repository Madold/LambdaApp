package com.markusw.lambda.core.domain

interface ChatClient {
    fun initChatClient(username: String, userId: String, photoUrl: String)
}