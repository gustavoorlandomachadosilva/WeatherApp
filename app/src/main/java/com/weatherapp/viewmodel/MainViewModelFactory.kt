package com.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weatherapp.api.WeatherService
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.monitor.ForecastMonitor

class MainViewModelFactory(
    private val db: FBDatabase,
    private val service: WeatherService,
    private val forecastMonitor: ForecastMonitor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                db,
                service,
                forecastMonitor
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}