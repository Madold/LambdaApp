package com.markusw.lambda.auth.data.repository

import com.google.firebase.auth.AuthCredential
import com.markusw.lambda.auth.domain.AuthService
import com.markusw.lambda.auth.domain.repository.AuthRepository
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.Result

class AndroidAuthRepository(
    private val authService: AuthService,
    private val remoteDatabase: RemoteDatabase
): AuthRepository {
    override suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit> {
        return try {
            authService.authenticateWithCredential(credential)
            val loggedUser = authService.getLoggedUser()
            loggedUser?.let { remoteDatabase.saveUser(it) }
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authService.logout()
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }
}