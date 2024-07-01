package com.markusw.lambda.home.domain.repository

import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.core.domain.model.Donation
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DonationRepository {
    suspend fun donate(
        donation: Donation
    ): Result<Unit>

    fun getDonations(): Flow<List<Donation>>
}