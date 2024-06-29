package com.markusw.lambda.auth.domain

import com.google.firebase.auth.AuthCredential

interface AuthService {
    suspend fun authenticateWithCredential(credential: AuthCredential)
    suspend fun logout()
}