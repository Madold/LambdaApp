package com.markusw.lambda.core.data.repository

import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.domain.repository.UsersRepository
import com.markusw.lambda.core.utils.Result
import kotlinx.coroutines.flow.Flow

class AndroidUsersRepository(
    private val remoteDatabase: RemoteDatabase
): UsersRepository {

    override fun getUsers(): Flow<List<User>> {
        return remoteDatabase.getUsers()
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            remoteDatabase.saveUser(user)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message.toString())
        }
    }
}