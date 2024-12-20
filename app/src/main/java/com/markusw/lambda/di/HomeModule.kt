package com.markusw.lambda.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.markusw.lambda.core.domain.local.LocalDatabase
import com.markusw.lambda.core.domain.remote.RemoteDatabase
import com.markusw.lambda.home.data.remote.FirebaseStorageService
import com.markusw.lambda.home.data.repository.AndroidAttendanceRepository
import com.markusw.lambda.home.data.repository.AndroidDonationRepository
import com.markusw.lambda.home.data.repository.AndroidMentoringRepository
import com.markusw.lambda.home.data.repository.AndroidPaymentRepository
import com.markusw.lambda.home.domain.remote.RemoteStorage
import com.markusw.lambda.home.domain.repository.AttendanceRepository
import com.markusw.lambda.home.domain.repository.DonationRepository
import com.markusw.lambda.home.domain.repository.MentoringRepository
import com.markusw.lambda.home.domain.repository.PaymentRepository
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

    @Provides
    @Singleton
    fun provideDonationsRepository(
        remoteDatabase: RemoteDatabase,
        localDatabase: LocalDatabase,
        @ApplicationContext context: Context
    ): DonationRepository {
        return AndroidDonationRepository(
            localDatabase,
            remoteDatabase,
            context
        )
    }

    @Provides
    @Singleton
    fun providePaymentRepository(
        remoteDatabase: RemoteDatabase,
        localDatabase: LocalDatabase,
        @ApplicationContext context: Context
    ): PaymentRepository {
        return AndroidPaymentRepository(
            remoteDatabase,
            localDatabase,
            context
        )
    }

    @Provides
    @Singleton
    fun provideAttendanceRepository(
        remoteDatabase: RemoteDatabase,
        localDatabase: LocalDatabase,
        @ApplicationContext context: Context
    ): AttendanceRepository {
        return AndroidAttendanceRepository(
            remoteDatabase,
            localDatabase,
            context
        )
    }

}