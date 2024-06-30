package com.markusw.lambda.core.domain.remote

import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
    fun getUsers(): Flow<List<User>>
    suspend fun saveUser(user: User)
    suspend fun insertMentoring(mentoring: Mentoring)
    fun getTutoringSessionsDto(): Flow<List<MentoringDto>>
}