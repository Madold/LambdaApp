package com.markusw.lambda.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.home.data.remote.FirebaseStorageService
import com.markusw.lambda.home.data.repository.AndroidMentoringRepository
import com.markusw.lambda.home.domain.remote.RemoteStorage
import com.markusw.lambda.home.domain.repository.MentoringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideMentoringRepository(
        remoteDatabase: RemoteDatabase,
        localDatabase: LocalDatabase,
        @ApplicationContext context: Context
    ): MentoringRepository {
        return AndroidMentoringRepository(
            remoteDatabase,
            localDatabase,
            context
        )
    }

    @Provides
    @Singleton
    fun provideRemoteStorage(): RemoteStorage {
        return FirebaseStorageService(Firebase.storage)
    }

}