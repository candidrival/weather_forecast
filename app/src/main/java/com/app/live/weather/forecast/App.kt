package com.app.live.weather.forecast

import android.app.Application
import com.app.live.weather.forecast.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                *appModule.toTypedArray()
            )
        }
    }

}