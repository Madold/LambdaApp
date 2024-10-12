package com.markusw.lambda.core.data

import android.content.Context
import com.markusw.lambda.core.domain.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.ChatClient as StreamChatClientInitializer

class StreamChatClient(
    private val context: Context
): ChatClient {

    private var client: io.getstream.chat.android.client.ChatClient? = null

    override fun initChatClient(username: String, userId: String, photoUrl: String) {
        client = StreamChatClientInitializer.Builder(
            apiKey = "y8ybwab9286t",
            context
        ).logLevel(ChatLogLevel.ALL).build()
    }


}