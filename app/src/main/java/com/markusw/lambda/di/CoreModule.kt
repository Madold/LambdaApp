package com.markusw.lambda.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.markusw.lambda.core.data.StreamVideoClient
import com.markusw.lambda.core.data.local.AndroidLocalDatabase
import com.markusw.lambda.core.data.remote.FireStoreDatabase
import com.markusw.lambda.core.data.repository.AndroidUsersRepository
import com.markusw.lambda.core.domain.VideoClient
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.domain.repository.UsersRepository
import com.markusw.lambda.db.LambdaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideVideoClient(@ApplicationContext context: Context): VideoClient {
        return StreamVideoClient(context)
    }

    @Provides
    @Singleton
    fun provideRemoteDatabase(): RemoteDatabase {
        return FireStoreDatabase(Firebase.firestore)
    }

    @Provides
    @Singleton
    fun provideUsersRepository(
        remoteDatabase: RemoteDatabase,
        localDatabase: LocalDatabase,
        @ApplicationContext context: Context
    ): UsersRepository {
        return AndroidUsersRepository(
            remoteDatabase,
            localDatabase,
            context
        )
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(sqlDriver: SqlDriver): LocalDatabase {
        return AndroidLocalDatabase(LambdaDatabase(sqlDriver))
    }

}