package com.markusw.lambda.core.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.ext.toDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.tasks.await

class FireStoreDatabase(
    private val firestore: FirebaseFirestore
) : RemoteDatabase {

    companion object {
        const val USERS_COLLECTIONS = "users"
        const val TUTORING = "tutoring"
    }

    override fun getUsers(): Flow<List<User>> {
        return callbackFlow {
            val snapshotListener = firestore
                .collection(USERS_COLLECTIONS)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let { querySnapshot ->
                        val users = querySnapshot
                            .toObjects(User::class.java)
                        trySend(users.toList())
                    }

                }

            awaitClose {
                snapshotListener.remove()
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

    override suspend fun insertMentoring(mentoring: Mentoring) {

        val docRef = firestore
            .collection(TUTORING)
            .add(mentoring.toDto())
            .await()

        docRef.update(
            mapOf(
                "roomId" to docRef.id
            ),
        ).await()
    }

    override fun getTutoringSessionsDto(): Flow<List<MentoringDto>> {
        return callbackFlow {
            val snapshotListener = firestore
                .collection(TUTORING)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let { querySnapshot ->
                        trySend(
                            querySnapshot
                            .toObjects(MentoringDto::class.java)
                            .filter { it.roomId.isNotBlank() }
                        )
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }
        }.conflate()
    }

    override suspend fun updateMentoring(mentoring: Mentoring) {
        firestore
            .collection(TUTORING)
            .document(mentoring.roomId)
            .update(
                mapOf(
                    "authorId" to mentoring.author?.id,
                    "coverUrl" to mentoring.coverUrl,
                    "description" to mentoring.description,
                    "price" to mentoring.price,
                    "requesterDescription" to mentoring.requesterDescription,
                    "requesterId" to mentoring.requester.id,
                    "title" to mentoring.title
                )
            ).await()
    }

}