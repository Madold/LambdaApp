package com.markusw.lambda.home.domain.repository

import com.markusw.lambda.core.domain.model.Mentoring
import kotlinx.coroutines.flow.Flow
import com.markusw.lambda.core.utils.Result

interface MentoringRepository {
    fun getTutoringSessions(): Flow<List<Mentoring>>
    suspend fun saveMentoring(mentoring: Mentoring): Result<Unit>
    suspend fun updateMentoring(mentoring: Mentoring): Result<Unit>
}