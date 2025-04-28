package com.app.live.weather.forecast.data.repositories

import com.app.live.weather.forecast.domain.ForecastResponse
import com.app.live.weather.forecast.domain.WeatherResponse
import com.app.live.weather.forecast.data.api.WeatherApiService
import com.app.live.weather.forecast.data.api.utils.GenericResponse

class WeatherRepository(private val api: WeatherApiService) {

    suspend fun getCurrentWeather(city: String): GenericResponse<WeatherResponse> {
        return api.getCurrentWeather(city)
    }

    suspend fun getForecast(city: String): GenericResponse<ForecastResponse> {
        return api.getForecast(city)
    }
}