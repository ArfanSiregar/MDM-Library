package com.kreditplus.mdmlibrary.data

import com.kreditplus.data.area.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiConfig {

    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .followRedirects(true)
        .followSslRedirects(true)
        .addInterceptor {
            val newUrl = it.switchUrl(
                url = "BASE_URL",
            )

            val request = it.requestHeader(
                switchUrl = newUrl,
                apiKey = "API_KEY"
            )

            it.proceed(request)
        }
        .loggingInterceptor()
        .build()

    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("BASE_URL")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    private fun Interceptor.Chain.switchUrl(url: String): HttpUrl {
        val host = url.toHttpUrl()
        return request()
            .url
            .newBuilder()
            .scheme(host.scheme)
            .host(host.toUrl().toURI().host)
            .build()
    }

    private fun Interceptor.Chain.requestHeader(
        switchUrl: HttpUrl,
        apiKey: String? = null,
    ): Request {
        val request = this.request()
            .newBuilder()
            .url(switchUrl)
            .addHeader(
                "MOBILE_APP_VERSION",
                "3.7.0"
            )

        if (apiKey != null) {
            request.addHeader("Apikey", apiKey)
        }
        return request.build()
    }

    private fun OkHttpClient.Builder.loggingInterceptor() = apply {
        if (BuildConfig.DEBUG) {
            this.addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }
    }
}