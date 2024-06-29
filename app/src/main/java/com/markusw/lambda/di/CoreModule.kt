package com.markusw.lambda.di

import android.content.Context
import com.markusw.lambda.core.data.StreamVideoClient
import com.markusw.lambda.core.domain.VideoClient
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


}