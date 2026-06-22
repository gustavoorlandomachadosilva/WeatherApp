package com.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.api.WeatherService

class MainViewModelFactory(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel>
            create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                MainViewModel::class.java
            )
        ) {

            return MainViewModel(
                db,
                service
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}