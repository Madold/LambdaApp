package com.markusw.lambda.auth.domain.repository

import com.google.firebase.auth.AuthCredential
import com.markusw.lambda.core.utils.Result

interface AuthRepository {
    suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit>
    suspend fun logout(): Result<Unit>
}