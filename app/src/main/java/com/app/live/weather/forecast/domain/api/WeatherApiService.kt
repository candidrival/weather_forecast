package com.app.live.weather.forecast.domain.api

import com.app.live.weather.forecast.BuildConfig
import com.app.live.weather.forecast.data.ForecastResponse
import com.app.live.weather.forecast.data.WeatherResponse
import com.app.live.weather.forecast.domain.api.utils.GenericResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): GenericResponse<WeatherResponse>

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): GenericResponse<ForecastResponse>
}
