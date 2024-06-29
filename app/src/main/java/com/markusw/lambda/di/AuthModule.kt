package com.markusw.lambda.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.markusw.lambda.auth.data.FirebaseAuthService
import com.markusw.lambda.auth.data.repository.AndroidAuthRepository
import com.markusw.lambda.auth.domain.AuthService
import com.markusw.lambda.auth.domain.repository.AuthRepository
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return FirebaseAuthService(Firebase.auth)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        remoteDatabase: RemoteDatabase
    ): AuthRepository {
        return AndroidAuthRepository(authService, remoteDatabase)
    }

}