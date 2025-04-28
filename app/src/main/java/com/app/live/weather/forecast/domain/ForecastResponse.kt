package com.app.live.weather.forecast.domain

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val clouds: Clouds,
    @SerializedName("dt_txt") val dateText: String
)

data class City(
    val name: String,
    val country: String
)
