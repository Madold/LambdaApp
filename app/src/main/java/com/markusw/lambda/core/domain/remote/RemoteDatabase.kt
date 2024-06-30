package com.markusw.lambda.core.domain.remote

import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
    fun getUsers(): Flow<List<User>>
    suspend fun saveUser(user: User)
    suspend fun saveMentoring(mentoring: Mentoring)
    fun getTutoringSessions(): Flow<List<Mentoring>>
}