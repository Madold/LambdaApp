package com.markusw.lambda.home.data.repository

import android.content.Context
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.core.utils.ext.isDeviceOnline
import com.markusw.lambda.home.domain.repository.MentoringRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class AndroidMentoringRepository(
    private val remoteDatabase: RemoteDatabase,
    private val localDatabase: LocalDatabase,
    private val context: Context,
) : MentoringRepository {

    override fun getTutoringSessions(): Flow<List<Mentoring>> {

        val remoteMentoringFlow = remoteDatabase
            .getPaymentsDto()
            .flatMapLatest { paymentsDto ->
                localDatabase.deleteAllMentoringPayments()
                localDatabase.insertMentoringPayments(paymentsDto)
                remoteDatabase.getTutoringSessionsDto()
            }.flatMapLatest { sessions ->
                localDatabase.deleteAllMentoring()
                localDatabase.insertTutorials(sessions)
                localDatabase.getTutorials()
            }

        val localMentoringFlow = localDatabase.getTutorials()

        return if (context.isDeviceOnline()) {
            remoteMentoringFlow
        } else {
            localMentoringFlow
        }
    }

    override suspend fun saveMentoring(mentoring: Mentoring): Result<Unit> {
        return try {
            remoteDatabase.insertMentoring(mentoring)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }

    override suspend fun updateMentoring(mentoring: Mentoring): Result<Unit> {
        return try {
            remoteDatabase.updateMentoring(mentoring)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }
}