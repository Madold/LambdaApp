package com.markusw.lambda.network.data.api

import com.markusw.lambda.core.utils.Result
import javax.inject.Inject

class LambdaBackendService(
    private val lambdaApi: LambdaApi
) {

    suspend fun startTranscription(
        roomId: String,
    ): Result<Unit> {
        return try {
            val call = lambdaApi.startTranscription(roomId)
            val result = call.execute()

            if (result.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(result.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun endTranscription(
        roomId: String,
    ): Result<Unit> {
        return try {
            val call = lambdaApi.endTranscription(roomId)
            val result = call.execute()

            if (result.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(result.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun sendCallInfoByEmail(
        to: List<String>,
        urlVideos: List<String>,
        roomId: String,
    ): Result<Unit> {
        return try {
            val call = lambdaApi.sendCallInfoByEmail(
                LambdaApi.CallInfoBody(
                    to = to,
                    urlVideos = urlVideos,
                    roomId = roomId,
                )
            )
            val result = call.execute()

            if (result.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(result.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

}