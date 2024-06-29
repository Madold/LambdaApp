package com.markusw.lambda.core.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.tasks.await

class FireStoreDatabase(
    private val firestore: FirebaseFirestore
) : RemoteDatabase {

    companion object {
        const val USERS_COLLECTIONS = "users"
    }

    override fun getUsers(): Flow<List<User>> {
        return callbackFlow {
            firestore
                .collection(USERS_COLLECTIONS)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let { querySnapshot ->
                        val users = querySnapshot
                            .toObjects(User::class.java)
                        trySend(users.toList())
                    }

                }
        }.conflate()
    }

    override suspend fun saveUser(user: User) {
        firestore
            .collection(USERS_COLLECTIONS)
            .document(user.id)
            .set(user)
            .await()
    }

}