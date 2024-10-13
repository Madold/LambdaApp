package com.markusw.lambda.core.data

import android.content.Context
import com.markusw.lambda.core.domain.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import io.getstream.chat.android.client.ChatClient as StreamChatClientInitializer

typealias ConnectionResult = Boolean

class StreamChatClient(
    private val context: Context
) : ChatClient {

    private var client: io.getstream.chat.android.client.ChatClient? = null

    override suspend fun initChatClient(username: String, userId: String): ConnectionResult {

        val offlinePluginFactory = StreamOfflinePluginFactory(appContext = context)
        val statePluginFactory =
            StreamStatePluginFactory(config = StatePluginConfig(), appContext = context)

        client = StreamChatClientInitializer
            .Builder(apiKey = "y8ybwab9286t", context)
            .logLevel(ChatLogLevel.ALL)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .build()

        val result = client?.connectGuestUser(
            userId = userId,
            username = username
        )?.await()
            ?.isSuccess

        return result ?: false
    }

    override fun disconnect() {
        client?.disconnect(true)
    }

}