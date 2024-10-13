package com.markusw.lambda.core.domain.remote

import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.model.Donation
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.home.data.model.AttendanceDto
import com.markusw.lambda.home.data.model.DonationDto
import com.markusw.lambda.home.data.model.MentoringPaymentDto
import com.markusw.lambda.video.WaitingConfirmation
import com.markusw.lambda.video.data.CallAccessDto
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
    fun getUsers(): Flow<List<User>>
    suspend fun getRemoteUserById(userId: String): User?
    suspend fun saveUser(user: User)
    suspend fun insertMentoring(mentoring: Mentoring)
    fun getTutoringSessionsDto(): Flow<List<MentoringDto>>
    suspend fun updateMentoring(mentoring: Mentoring)
    suspend fun saveDonation(donation: Donation)
    fun getDonationsDto(): Flow<List<DonationDto>>
    suspend fun savePayment(mentoring: Mentoring, user: User)
    fun getPaymentsDto(): Flow<List<MentoringPaymentDto>>
    suspend fun registerAttendanceDto(attendanceDto: AttendanceDto)
    fun getAttendanceDto(): Flow<List<AttendanceDto>>
    fun getCallStateById(roomId: String): Flow<String>
    suspend fun finishCall(roomId: String)
    suspend fun deleteMentoringById(roomId: String)
    fun getCallAccess(roomId: String, userId: String): Flow<String>
    suspend fun registerAccess(access: CallAccessDto)
    suspend fun checkAccessExist(roomId: String, userId: String): Boolean
    fun getWaitingConfirmations(roomId: String): Flow<List<WaitingConfirmation>>
    suspend fun acceptCall(dto: CallAccessDto)
    suspend fun rejectCall(dto: CallAccessDto)
    suspend fun startCall(roomId: String)
}