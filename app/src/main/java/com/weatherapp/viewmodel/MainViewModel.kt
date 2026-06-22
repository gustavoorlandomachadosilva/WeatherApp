package com.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.weatherapp.api.WeatherService
import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.FBUser
import com.weatherapp.db.fb.toFBCity
import com.weatherapp.model.City
import com.weatherapp.model.User
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModel(),
    FBDatabase.Listener {

    private val _cities =
        mutableStateListOf<City>()

    val cities
        get() = _cities.toList()

    private val _user =
        mutableStateOf<User?>(null)

    val user: User?
        get() = _user.value

    init {
        db.setListener(this)
    }

    fun remove(city: City) {
        db.remove(
            city.toFBCity()
        )
    }

    fun addCity(name: String) {

        service.getLocation(name) {
                lat,
                lng ->

            if (
                lat != null &&
                lng != null
            ) {

                db.add(
                    City(
                        name = name,
                        location = LatLng(
                            lat,
                            lng
                        )
                    ).toFBCity()
                )
            }
        }
    }

    fun addCity(
        location: LatLng
    ) {

        service.getName(
            location.latitude,
            location.longitude
        ) { name ->

            if (name != null) {

                db.add(
                    City(
                        name = name,
                        location = location
                    ).toFBCity()
                )
            }
        }
    }

    override fun onUserLoaded(
        user: FBUser
    ) {
        _user.value =
            user.toUser()
    }

    override fun onUserSignOut() {
        _cities.clear()
        _user.value = null
    }

    override fun onCityAdded(
        city: FBCity
    ) {
        _cities.add(
            city.toCity()
        )
    }

    override fun onCityUpdated(
        city: FBCity
    ) {

        val index =
            _cities.indexOfFirst {
                it.name == city.name
            }

        if (index >= 0) {
            _cities[index] =
                city.toCity()
        }
    }

    override fun onCityRemoved(
        city: FBCity
    ) {
        _cities.removeAll {
            it.name == city.name
        }
    }
}