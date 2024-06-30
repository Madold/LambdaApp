package com.markusw.lambda.home.data.repository

import android.content.Context
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.home.domain.repository.MentoringRepository
import kotlinx.coroutines.flow.Flow

class AndroidMentoringRepository(
    private val remoteDatabase: RemoteDatabase,
    private val localDatabase: LocalDatabase,
    private val context: Context
): MentoringRepository {

    override fun getTutoringSessions(): Flow<List<Mentoring>> {
        return remoteDatabase
            .getTutoringSessions()
    }

    override suspend fun saveMentoring(mentoring: Mentoring): Result<Unit> {
        return try {
            remoteDatabase.saveMentoring(mentoring)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }
}