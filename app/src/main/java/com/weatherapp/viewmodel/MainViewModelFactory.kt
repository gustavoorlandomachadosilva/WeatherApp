package com.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weatherapp.db.fb.FBDatabase

class MainViewModelFactory(
    private val db: FBDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}