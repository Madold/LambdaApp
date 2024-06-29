package com.markusw.lambda.auth.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.markusw.lambda.auth.domain.AuthService
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.utils.ext.toDomainModel
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth
): AuthService {

    override suspend fun authenticateWithCredential(credential: AuthCredential) {
        firebaseAuth
            .signInWithCredential(credential)
            .await()
    }

    override suspend fun logout() {
        firebaseAuth
            .signOut()
    }

    override fun getLoggedUser(): User? {
        return firebaseAuth.currentUser?.toDomainModel()
    }


}