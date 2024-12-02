package com.markusw.lambda.core.domain

import com.markusw.lambda.core.utils.Result
import io.getstream.video.android.core.Call

interface VideoClient {
    fun initVideoClient(username: String, userId: String, photoUrl: String)
    suspend fun joinCall(): Result<Call>
    fun disconnect(): Result<Unit>
    fun getCall(): Call?
    fun callToRoom(roomId: String)
    suspend fun finishCall(): Result<Unit>
    suspend fun listRecordings(roomId: String): Result<List<String>>
}