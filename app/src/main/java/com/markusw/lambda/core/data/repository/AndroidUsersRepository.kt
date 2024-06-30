package com.markusw.lambda.core.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.domain.repository.UsersRepository
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.core.utils.ext.isDeviceOnline
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class AndroidUsersRepository(
    private val remoteDatabase: RemoteDatabase,
    private val localDatabase: LocalDatabase,
    private val context: Context
): UsersRepository {

    override fun getUsers(): Flow<List<User>> {
        val remoteUsersFlow = remoteDatabase
            .getUsers()
            .flatMapLatest { remoteUsers ->
                localDatabase.deleteAllUsers()
                localDatabase.insertUsers(remoteUsers)
                localDatabase.getUsers()
            }

        val localUsersFlow = localDatabase.getUsers()

        return if (context.isDeviceOnline()) {
            remoteUsersFlow
        } else {
            localUsersFlow
        }
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