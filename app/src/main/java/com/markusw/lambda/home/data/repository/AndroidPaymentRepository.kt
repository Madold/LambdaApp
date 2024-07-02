@file:Suppress("OPT_IN_USAGE")

package com.markusw.lambda.home.data.repository

import android.content.Context
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.home.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.flatMapLatest

class AndroidPaymentRepository(
    private val remoteDatabase: RemoteDatabase,
    private val localDatabase: LocalDatabase,
    private val context: Context
) : PaymentRepository {

    override suspend fun payMentoringAccess(mentoring: Mentoring, user: User): Result<Unit> {
        return try {
            val paymentExists = localDatabase.checkPaymentIfExist(mentoringId = mentoring.roomId, userId = user.id)
            if (!paymentExists) {
                remoteDatabase.savePayment(mentoring, user)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }
}