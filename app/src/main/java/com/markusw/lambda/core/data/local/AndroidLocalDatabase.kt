package com.markusw.lambda.core.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.utils.ext.toDomainModel
import com.markusw.lambda.db.LambdaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AndroidLocalDatabase(
    private val database: LambdaDatabase
): LocalDatabase {

    private val queries = database.lambdaDatabaseQueries

    override fun getUsers(): Flow<List<User>> {
        return queries
            .getUsers()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { it.toDomainModel() }
            }
    }

    override fun insertUsers(users: List<User>) {
        database.transaction {
            users.forEach { user ->
                queries.insertUser(
                    id = user.id,
                    displayName = user.displayName,
                    photoUrl = user.photoUrl,
                    email = user.email
                )
            }
        }
    }


}