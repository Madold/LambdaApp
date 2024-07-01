package com.markusw.lambda.core.domain.local

import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.model.Donation
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.home.data.model.DonationDto
import kotlinx.coroutines.flow.Flow

interface LocalDatabase {
    fun getUsers(): Flow<List<User>>
    fun insertUsers(users: List<User>)
    fun deleteAllUsers()
    fun insertTutorials(mentoringDto: List<MentoringDto>)
    fun deleteAllMentoring()
    fun getUserById(userId: String): User
    fun getParticipantsByMentoringId(mentoringId: String): List<User>
    fun getTutorials(): Flow<List<Mentoring>>
    fun insertDonations(donationsDto: List<DonationDto>)
    fun deleteAllDonations()
    fun getDonations(): Flow<List<Donation>>
    fun getMentoringByRoomId(roomId: String): Mentoring
}