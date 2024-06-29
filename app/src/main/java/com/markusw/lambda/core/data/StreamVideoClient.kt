package com.markusw.lambda.core.data

import android.content.Context
import com.markusw.lambda.core.domain.VideoClient
import com.markusw.lambda.core.utils.Result
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.UserType
import io.getstream.video.android.model.User as StreamSdkUser

class StreamVideoClient(
    private val context: Context
): VideoClient {

    private var client: StreamVideo? = null
    private var call: Call? = null

    override fun initVideoClient(username: String) {
        if (client == null) {
            StreamVideo.removeClient()

            client = StreamVideoBuilder(
                context = context,
                apiKey = "kegs4gg73yzr",
                user = StreamSdkUser(
                    id = username,
                    name = username,
                    type = UserType.Guest
                )
            ).build()

            callToRoom("main-room")
        }
    }

    override fun callToRoom(roomId: String) {
        call = client?.call("default", roomId)
    }

    override suspend fun joinCall(): Result<Call> {
        return try {
            val shouldCreate = client
                ?.queryCalls(filters = emptyMap())
                ?.getOrNull()
                ?.calls
                ?.isEmpty() == true

            call?.join(create = shouldCreate)?.isSuccess

            Result.Success(call ?: throw Exception("Call was null"))
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override fun disconnect(): Result<Unit> {
        return try {
            call?.leave()
            client?.logOut()
            client = null
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override fun getCall(): Call? {
        return call
    }

}