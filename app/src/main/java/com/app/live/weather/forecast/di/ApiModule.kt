package com.app.live.weather.forecast.di

import com.app.live.weather.forecast.data.api.WeatherApiService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single<WeatherApiService> {
        get<Retrofit>().create(
            WeatherApiService::class.java
        )
    }
}