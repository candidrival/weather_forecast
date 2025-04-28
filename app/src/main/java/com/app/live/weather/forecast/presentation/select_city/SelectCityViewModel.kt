package com.app.live.weather.forecast.presentation.select_city

import com.app.live.weather.forecast.data.prefs.PreferenceWrapper
import com.app.live.weather.forecast.utils.base.BaseViewModel

class SelectCityViewModel(
    private val preferenceWrapper: PreferenceWrapper
): BaseViewModel() {

    fun putCity(city: String) {
        preferenceWrapper.putSelectedCity(city)
    }

    fun getCity() = preferenceWrapper.getSelectedCity()

}