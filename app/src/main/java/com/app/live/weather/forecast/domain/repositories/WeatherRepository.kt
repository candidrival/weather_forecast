package com.app.live.weather.forecast.domain.repositories

import com.app.live.weather.forecast.data.ForecastResponse
import com.app.live.weather.forecast.data.WeatherResponse
import com.app.live.weather.forecast.domain.api.WeatherApiService
import com.app.live.weather.forecast.domain.api.utils.GenericResponse

class WeatherRepository(private val api: WeatherApiService) {

    suspend fun getCurrentWeather(city: String): GenericResponse<WeatherResponse> {
        return api.getCurrentWeather(city)
    }

    suspend fun getForecast(city: String): GenericResponse<ForecastResponse> {
        return api.getForecast(city)
    }
}