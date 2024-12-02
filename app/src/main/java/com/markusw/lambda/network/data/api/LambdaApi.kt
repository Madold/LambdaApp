package com.markusw.lambda.network.data.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LambdaApi {

    data class CallInfoBody(
        val to: List<String>,
        val urlVideos: List<String>,
        val roomId: String,
    )

    @POST("calls/start-transcription/{roomId}")
    fun startTranscription(
        @Path("roomId") roomId: String,
    ): Call<ResponseBody>

    @POST("calls/end-transcription/{roomId}")
    fun endTranscription(
        @Path("roomId") roomId: String,
    ): Call<ResponseBody>

    @POST("emails")
    fun sendCallInfoByEmail(
        @Body callInfo: CallInfoBody,
    ): Call<ResponseBody>

}