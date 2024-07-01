package com.markusw.lambda.core.domain.remote

import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.model.Donation
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.home.data.model.DonationDto
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
    fun getUsers(): Flow<List<User>>
    suspend fun saveUser(user: User)
    suspend fun insertMentoring(mentoring: Mentoring)
    fun getTutoringSessionsDto(): Flow<List<MentoringDto>>
    suspend fun updateMentoring(mentoring: Mentoring)
    suspend fun saveDonation(donation: Donation)
    fun getDonationsDto(): Flow<List<DonationDto>>
}