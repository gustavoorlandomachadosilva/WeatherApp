package com.weatherapp.viewmodel

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.weatherapp.model.City
import com.google.android.gms.maps.model.LatLng

private fun getCities() = List(20) { i ->
    City(
        name = "Cidade $i",
        weather = "Carregando clima..."
    )
}

class MainViewModel : ViewModel() {

    private val _cities = getCities().toMutableStateList()

    val cities
        get() = _cities.toList()

    fun remove(city: City) {
        _cities.remove(city)
    }

    fun add(name: String, location: LatLng? = null) {
        _cities.add(City(name = name, location = location))
    }
}

