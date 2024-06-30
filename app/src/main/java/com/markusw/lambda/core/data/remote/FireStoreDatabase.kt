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
            val snapshotListener =  firestore
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

    override suspend fun saveMentoring(mentoring: Mentoring) {

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

    override fun getTutoringSessions(): Flow<List<Mentoring>> {
        return callbackFlow {
            val snapshotListener = firestore
                .collection(TUTORING)
                .addSnapshotListener { value, error ->

                    error?.let { close(it) }

                    value?.let { querySnapshot ->

                        val mentoringDto = querySnapshot
                            .toObjects(MentoringDto::class.java)

                        val tutoring =  mentoringDto.map { dto ->
                            Mentoring(
                                title = dto.title,
                                roomId = dto.roomId,
                                price = dto.price,
                                requesterDescription = dto.requesterDescription,
                                description = dto.description,
                                coverUrl = dto.coverUrl
                            )
                        }

                        trySend(tutoring)
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }
        }.conflate()
    }

}