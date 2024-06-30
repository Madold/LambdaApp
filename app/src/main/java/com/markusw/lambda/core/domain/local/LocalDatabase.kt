package com.markusw.lambda.core.domain.local

import com.markusw.lambda.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LocalDatabase {
    fun getUsers(): Flow<List<User>>
    fun insertUsers(users: List<User>)
    fun deleteAllUsers()
}