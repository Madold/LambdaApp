package com.markusw.lambda.auth.domain

import com.google.firebase.auth.AuthCredential
import com.markusw.lambda.core.domain.model.User

interface AuthService {
    suspend fun authenticateWithCredential(credential: AuthCredential)
    suspend fun logout()
    fun getLoggedUser(): User?
}