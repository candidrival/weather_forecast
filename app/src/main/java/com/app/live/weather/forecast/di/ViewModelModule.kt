package com.app.live.weather.forecast.di

import com.app.live.weather.forecast.ActivityViewModel
import com.app.live.weather.forecast.presentation.main.MainViewModel
import com.app.live.weather.forecast.presentation.select_city.SelectCityViewModel
import com.app.live.weather.forecast.presentation.splash.SplashViewModel
import com.app.live.weather.forecast.presentation.today_weather.TodayWeatherViewModel
import com.app.live.weather.forecast.presentation.weeks_forecast.ForecastViewModel
import com.app.live.weather.forecast.utils.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * ViewModels module
 */
val viewModel: Module = module {
    viewModel { BaseViewModel() }
    viewModel { ActivityViewModel() }
    viewModel { SplashViewModel() }
    viewModel { MainViewModel() }
    viewModel { TodayWeatherViewModel(get(), get()) }
    viewModel { ForecastViewModel(get(), get()) }
    viewModel { SelectCityViewModel(get()) }
}
