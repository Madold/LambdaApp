package com.markusw.lambda.core.data

import android.content.Context
import android.util.Log
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
    private lateinit var roomId: String

    companion object {
        const val TAG = "StreamVideoClient"
    }

    override fun initVideoClient(username: String, userId: String, photoUrl: String) {
        if (client == null) {
            StreamVideo.removeClient()

            client = StreamVideoBuilder(
                context = context,
                apiKey = "y8ybwab9286t",
                user = StreamSdkUser(
                    id = userId,
                    name = username,
                    type = UserType.Guest,
                    image = photoUrl
                )
            ).build()
        }
    }

    override fun callToRoom(roomId: String) {
        this.roomId = roomId
        call = client?.call("default", roomId)
    }

    override suspend fun finishCall(): Result<Unit> {
        return try {
            call?.end()
            resetVideoClient()

            val result = call?.listRecordings()
            result?.onSuccess { response ->
                response.recordings.forEach { recording ->
                    Log.d(TAG, recording.url)
                }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }

    override suspend fun joinCall(): Result<Call> {
        return try {

            val shouldCreate = client
                ?.queryCalls(filters = mapOf(
                    "id" to roomId
                ))
                ?.getOrNull()
                ?.calls
                ?.isEmpty() == true

            val joinResult = call?.join(
                create = shouldCreate
            )
            val isSuccess = joinResult?.isSuccess ?: false

            if (!isSuccess) {
                throw Exception(joinResult?.errorOrNull()?.message)
            }

            Result.Success(call ?: throw Exception("Call was null"))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }

    override fun disconnect(): Result<Unit> {
        return try {
            call?.leave()
            resetVideoClient()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    private fun resetVideoClient() {
        client?.logOut()
        client = null
    }

    override fun getCall(): Call? {
        return call
    }

}