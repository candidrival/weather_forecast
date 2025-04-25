package com.app.live.weather.forecast.di

import com.app.live.weather.forecast.domain.prefs.PreferenceWrapper
import com.app.live.weather.forecast.domain.prefs.SecuritySharedPreferences
import com.app.live.weather.forecast.domain.prefs.SecuritySharedPreferencesImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val components: Module = module {
    single<SecuritySharedPreferences> {
        SecuritySharedPreferencesImpl(
            get(),
            "secure_ether_forecast_pref"
        )
    }
    single { PreferenceWrapper(get()) }
}
