package com.markusw.lambda.di

import com.markusw.lambda.network.data.api.LambdaApi
import com.markusw.lambda.network.data.api.LambdaBackendService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLambdaApi(): LambdaApi {

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://lambdabackend-production.up.railway.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(LambdaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLambdaBackendService(lambdaApi: LambdaApi): LambdaBackendService {
        return LambdaBackendService(lambdaApi)
    }

}