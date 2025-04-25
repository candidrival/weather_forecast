package com.app.live.weather.forecast.presentation.weeks_forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.live.weather.forecast.data.ForecastItem
import com.app.live.weather.forecast.data.ForecastResponse
import com.app.live.weather.forecast.data.WeatherResponse
import com.app.live.weather.forecast.domain.api.utils.NetworkResponse
import com.app.live.weather.forecast.domain.prefs.PreferenceWrapper
import com.app.live.weather.forecast.domain.repositories.WeatherRepository
import com.app.live.weather.forecast.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForecastViewModel(
    private val weatherRepository: WeatherRepository,
    private val preferenceWrapper: PreferenceWrapper
) : BaseViewModel() {
    private val _forecast = MutableLiveData<ForecastResponse>()
    val forecast: LiveData<ForecastResponse> = _forecast

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadForecast(onError: () -> Unit) {
        _isLoading.value = true
        safeCall("loadWeather") {
            val city = preferenceWrapper.getSelectedCity()
            if (city.isNullOrEmpty()) {
                onError.invoke()
                _isLoading.value = false
            } else {
                when (val result = weatherRepository.getForecast(city)) {
                    is NetworkResponse.Success -> {
                        val filtered = result.body
                        _forecast.postValue(filtered)
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