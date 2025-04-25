package com.app.live.weather.forecast.domain.prefs

class PreferenceWrapper(
    private val preferences: SecuritySharedPreferences
) {
    fun putSelectedCity(city: String) = preferences.putString(KEY_SELECTED_CITY, city)
    fun getSelectedCity(): String? = preferences.getString(KEY_SELECTED_CITY, "")
}

private const val KEY_SELECTED_CITY = "KEY_SELECTED_CITY"