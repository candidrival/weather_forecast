package com.app.live.weather.forecast.di

import com.app.live.weather.forecast.data.api.utils.NetworkResponseAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.app.live.weather.forecast.BuildConfig
import com.app.live.weather.forecast.data.api.utils.TruncatedLoggingInterceptor

const val WRITE_TIMEOUT = 600L

val networkModule = module {
    val loggingInterceptor = TruncatedLoggingInterceptor()

    single {
        OkHttpClient.Builder()

            .addInterceptor(loggingInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS) // Add this
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit>() {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get<OkHttpClient>())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}