package com.markusw.lambda.core.domain

import com.markusw.lambda.core.data.ConnectionResult

interface ChatClient {
    suspend fun initChatClient(username: String, userId: String): ConnectionResult
    fun disconnect()
}