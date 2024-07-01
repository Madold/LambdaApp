package com.markusw.lambda.core.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.markusw.lambda.core.data.model.MentoringDto
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.utils.ext.toDomainModel
import com.markusw.lambda.db.LambdaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AndroidLocalDatabase(
    private val database: LambdaDatabase
) : LocalDatabase {

    private val queries = database.lambdaDatabaseQueries

    companion object {
        const val TAG = "LocalDatabaseImpl"
    }

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

    override fun deleteAllUsers() {
        queries.deleteAllUsers()
    }

    override fun insertTutorials(mentoringDto: List<MentoringDto>) {
        database.transaction {
            mentoringDto.forEach { dto ->
                queries
                    .insertMentoring(
                        roomId = dto.roomId,
                        title = dto.title,
                        description = dto.description,
                        requesterDescription = dto.requesterDescription,
                        price = dto.price,
                        coverUrl = dto.coverUrl,
                        requesterId = dto.requesterId,
                        authorId = dto.authorId
                    )
            }
        }

    }

    override fun deleteAllMentoring() {
        queries.deleteAllMentoring()
    }

    override fun getUserById(userId: String): User {
        return queries
            .getUserById(userId)
            .executeAsOne()
            .toDomainModel()
    }

    override fun getParticipantsByMentoringId(mentoringId: String): List<User> {
        return queries
            .getParticipantsByMentoringId(mentoringId)
            .executeAsList()
            .map { it.toDomainModel() }
    }

    override fun getTutorials(): Flow<List<Mentoring>> {
        return queries
            .getAllTutorialsWithDetails()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { tutorialDetails ->
                tutorialDetails.map { tutorialDetail ->
                    val participants = queries
                        .getParticipantsByMentoringId(tutorialDetail.roomId)
                        .executeAsList()
                        .map { it.toDomainModel() }
                    val requester = queries
                        .getUserById(tutorialDetail.requesterId)
                        .executeAsOne()
                        .toDomainModel()
                    val author = tutorialDetail
                        .authorId?.let {
                            queries.getUserById(it).executeAsOne()
                        }?.toDomainModel()

                    Mentoring(
                        roomId = tutorialDetail.roomId,
                        participants = participants,
                        requester = requester,
                        author = author,
                        price = tutorialDetail.price,
                        coverUrl = tutorialDetail.coverUrl,
                        totalRevenue = tutorialDetail.totalRevenue.toLong(),
                        description = tutorialDetail.description,
                        requesterDescription = tutorialDetail.requesterDescription,
                        title = tutorialDetail.title
                    )
                }


            }
    }


}