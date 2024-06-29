package com.markusw.lambda.core.domain.repository

import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.utils.Result
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun saveUser(user: User): Result<Unit>
}