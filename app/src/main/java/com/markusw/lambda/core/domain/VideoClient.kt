package com.markusw.lambda.core.domain

import com.markusw.lambda.core.utils.Result
import io.getstream.video.android.core.Call

interface VideoClient {
    fun initVideoClient(username: String)
    suspend fun joinCall(): Result<Call>
    fun disconnect(): Result<Unit>
    fun getCall(): Call?
}