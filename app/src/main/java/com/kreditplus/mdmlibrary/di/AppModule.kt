package com.kreditplus.mdmlibrary.di

import com.kreditplus.base.network.qualifier.Apigee
import com.kreditplus.mdmlibrary.data.ApiConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMoshi(): Moshi {
        return ApiConfig.provideMoshi()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return ApiConfig.provideOkHttpClient()
    }

    @Provides
    @Singleton
    @Apigee
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return ApiConfig.provideRetrofit(
            okHttpClient = okHttpClient,
            moshi = moshi
        )
    }
}