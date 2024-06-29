package com.markusw.lambda.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.markusw.lambda.core.data.StreamVideoClient
import com.markusw.lambda.core.data.remote.FireStoreDatabase
import com.markusw.lambda.core.data.repository.AndroidUsersRepository
import com.markusw.lambda.core.domain.VideoClient
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.core.domain.repository.UsersRepository
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
    fun provideUsersRepository(remoteDatabase: RemoteDatabase): UsersRepository {
        return AndroidUsersRepository(remoteDatabase)
    }

}