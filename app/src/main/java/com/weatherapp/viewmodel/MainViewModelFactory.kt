package com.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weatherapp.api.WeatherService
import com.weatherapp.monitor.ForecastMonitor
import com.weatherapp.repo.Repository

class MainViewModelFactory(
    private val repository: Repository,
    private val service: WeatherService,
    private val monitor: ForecastMonitor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                repository,
                service,
                monitor
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}