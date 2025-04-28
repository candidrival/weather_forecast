package com.app.live.weather.forecast.presentation.today_weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.live.weather.forecast.domain.WeatherResponse
import com.app.live.weather.forecast.data.api.utils.NetworkResponse
import com.app.live.weather.forecast.data.prefs.PreferenceWrapper
import com.app.live.weather.forecast.data.repositories.WeatherRepository
import com.app.live.weather.forecast.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodayWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val preferenceWrapper: PreferenceWrapper
) : BaseViewModel() {
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadWeather(onError: () -> Unit) {
        _isLoading.value = true
        safeCall("loadWeather") {
            val city = preferenceWrapper.getSelectedCity()
            if (city.isNullOrEmpty()) {
                onError.invoke()
                _isLoading.value = false
            } else {
                when (val result = weatherRepository.getCurrentWeather(city)) {
                    is NetworkResponse.Success -> {
                        val response = result.body
                        _weather.postValue(response)
                        _isLoading.value = false
                    }

                    else -> {
                        onError.invoke()
                        _isLoading.value = false
                    }
                }
            }
        }
    }

}