package com.app.live.weather.forecast.di

import com.app.live.weather.forecast.domain.repositories.WeatherRepository
import org.koin.dsl.module

val repository = module {
    single { WeatherRepository(get()) }
}