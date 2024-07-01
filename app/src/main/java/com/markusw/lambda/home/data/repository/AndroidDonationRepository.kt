@file:OptIn(ExperimentalCoroutinesApi::class)

package com.markusw.lambda.home.data.repository

import android.content.Context
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.model.Donation
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.ext.isDeviceOnline
import com.markusw.lambda.home.domain.repository.DonationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class AndroidDonationRepository(
    private val localDatabase: LocalDatabase,
    private val remoteDatabase: RemoteDatabase,
    private val context: Context
): DonationRepository {

    override fun getDonations(): Flow<List<Donation>> {
        val remoteDonationsFlow = remoteDatabase
            .getDonationsDto()
            .flatMapLatest { remoteDto ->
                localDatabase.deleteAllDonations()
                localDatabase.insertDonations(remoteDto)
                localDatabase.getDonations()
            }

        val localDonationsFlow = localDatabase.getDonations()

        return if (context.isDeviceOnline()) {
            remoteDonationsFlow
        } else {
            localDonationsFlow
        }
    }

    override suspend fun donate(donation: Donation): Result<Unit> {
        return try {
            remoteDatabase.saveDonation(donation)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }

}