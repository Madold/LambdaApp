package com.markusw.lambda.core.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.model.Donation
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.ext.toDto
import com.markusw.lambda.home.data.model.AttendanceDto
import com.markusw.lambda.home.data.model.DonationDto
import com.markusw.lambda.home.data.model.MentoringPaymentDto
import com.markusw.lambda.video.WaitingConfirmation
import com.markusw.lambda.video.data.CallAccessDto
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
        const val DONATIONS_COLLECTION = "donate"
        const val MENTORING_PAYMENTS_COLLECTION = "mentoring_payment"
        const val ATTENDS_COLLECTION = "attends"
        const val WAITING_ROOMS = "waiting_rooms"
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

    override suspend fun getRemoteUserById(userId: String): User? {
        return firestore
            .collection(USERS_COLLECTIONS)
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)
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

    override suspend fun saveDonation(donation: Donation) {
        firestore
            .collection(DONATIONS_COLLECTION)
            .add(
                mapOf(
                    "userId" to donation.author.id,
                    "amount" to donation.amount,
                    "mentoringId" to donation.mentoring.roomId
                )
            ).await()
    }

    override fun getDonationsDto(): Flow<List<DonationDto>> {
        return callbackFlow {
            val snapshotListener = firestore
                .collection(DONATIONS_COLLECTION)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let {
                        trySend(it.toObjects(DonationDto::class.java))
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }

        }.conflate()
    }

    override suspend fun savePayment(mentoring: Mentoring, user: User) {
        firestore
            .collection(MENTORING_PAYMENTS_COLLECTION)
            .add(
                mapOf(
                    "userId" to user.id,
                    "mentoringId" to mentoring.roomId
                )
            ).await()
    }

    override fun getPaymentsDto(): Flow<List<MentoringPaymentDto>> {
        return callbackFlow {
            val snapshotListener = firestore
                .collection(MENTORING_PAYMENTS_COLLECTION)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let {
                        trySend(it.toObjects(MentoringPaymentDto::class.java))
                    }

                }
            awaitClose {
                snapshotListener.remove()
            }
        }.conflate()
    }

    override suspend fun registerAttendanceDto(attendanceDto: AttendanceDto) {
        firestore
            .collection(ATTENDS_COLLECTION)
            .add(attendanceDto)
            .await()
    }

    override fun getAttendanceDto(): Flow<List<AttendanceDto>> {
        return callbackFlow<List<AttendanceDto>> {
            val snapshotListener = firestore
                .collection(ATTENDS_COLLECTION)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let {
                        trySend(it.toObjects(AttendanceDto::class.java))
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }
        }.conflate()
    }

    override fun getCallStateById(roomId: String): Flow<String> {
        return callbackFlow {
            val snapshotListener = firestore
                .collection(TUTORING)
                .document(roomId)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let {
                        val mentoring = it.toObject(MentoringDto::class.java)
                        trySend(mentoring?.state ?: "finished")
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }
        }.conflate()
    }

    override suspend fun finishCall(roomId: String) {
        firestore
            .collection(TUTORING)
            .document(roomId)
            .update(
                mapOf(
                    "state" to "finished"
                )
            ).await()
    }

    override suspend fun deleteMentoringById(roomId: String) {
        firestore
            .collection(TUTORING)
            .document(roomId)
            .delete()
            .await()
    }

    override fun getCallAccess(roomId: String, userId: String): Flow<String> {
        return callbackFlow {
            val snapshotListener = firestore
                .collection(WAITING_ROOMS)
                .document("$roomId$userId")
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let { document ->
                        trySend(
                            document.toObject(CallAccessDto::class.java)?.accessState ?: "waiting"
                        )
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }
        }.conflate()
    }

    override suspend fun registerAccess(accessDto: CallAccessDto) {
        firestore
            .collection(WAITING_ROOMS)
            .document("${accessDto.roomId}${accessDto.user.id}")
            .set(accessDto)
            .await()
    }

    override suspend fun checkAccessExist(roomId: String, userId: String): Boolean {
        return firestore
            .collection(WAITING_ROOMS)
            .document("$roomId$userId")
            .get()
            .await()
            .exists()
    }

    override fun getWaitingConfirmations(roomId: String): Flow<List<WaitingConfirmation>> {
        return callbackFlow {
            val snapshotListener =  firestore
                .collection(WAITING_ROOMS)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let { snapshot ->
                        val dtos = snapshot.toObjects(CallAccessDto::class.java)

                        val result = dtos
                            .filter { it.roomId == roomId && it.accessState != "granted" }
                            .map { WaitingConfirmation(dto = it) }

                        trySend(result)
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }

        }.conflate()

    }

    override suspend fun acceptCall(dto: CallAccessDto) {
        firestore
            .collection(WAITING_ROOMS)
            .document("${dto.roomId}${dto.user.id}")
            .update(
                mapOf(
                    "accessState" to "granted"
                )
            ).await()
    }

    override suspend fun rejectCall(dto: CallAccessDto) {
        firestore
            .collection(WAITING_ROOMS)
            .document("${dto.roomId}${dto.user.id}")
            .update(
                mapOf(
                    "accessState" to "rejected"
                )
            ).await()
    }

    override suspend fun startCall(roomId: String) {
        firestore
            .collection(TUTORING)
            .document(roomId)
            .update(
                mapOf(
                    "state" to "running"
                )
            )
    }


}