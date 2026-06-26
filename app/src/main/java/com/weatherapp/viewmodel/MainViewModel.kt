package com.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.api.WeatherService
import com.weatherapp.api.toWeather
import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.FBUser
import com.weatherapp.db.fb.toFBCity
import com.weatherapp.model.City
import com.weatherapp.model.User
import com.weatherapp.model.Weather
import com.weatherapp.model.Forecast
import com.weatherapp.api.toForecast

class MainViewModel(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModel(),
    FBDatabase.Listener {

    private val _cities =
        mutableStateMapOf<String, City>()

    val cities
        get() =
            _cities
                .values
                .toList()
                .sortedBy {
                    it.name
                }

    private val _user =
        mutableStateOf<User?>(null)

    private val _weather =
        mutableStateMapOf<String, Weather>()

    private val _forecast =
        mutableStateMapOf<String, List<Forecast>?>()

    private var _city =
        mutableStateOf<String?>(null)

    var city: String?
        get() = _city.value
        set(value) {
            _city.value = value
        }

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

        service.getLocation(name) { lat,
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

    private fun loadWeather(
        name: String
    ) {

        service.getWeather(name) {

            it?.let {

                _weather[name] =
                    it.toWeather()
            }
        }
    }

    private fun loadForecast(
        name: String
    ) {

        service.getForecast(name) {

            it?.let {

                _forecast[name] =
                    it.toForecast()
            }
        }
    }

    fun weather(
        name: String
    ) =
        _weather.getOrPut(name) {

            loadWeather(name)

            Weather.LOADING
        }

    fun forecast(
        name: String
    ) =
        _forecast.getOrPut(name) {

            loadForecast(name)

            emptyList()
        }

    override fun onUserLoaded(
        user: FBUser
    ) {

        _user.value =
            user.toUser()
    }

    override fun onUserSignOut() {

        _cities.clear()

        _weather.clear()

        _user.value = null
    }

    override fun onCityAdded(
        city: FBCity
    ) {

        _cities[city.name!!] =
            city.toCity()
    }

    override fun onCityUpdated(
        city: FBCity
    ) {

        _cities.remove(
            city.name
        )

        _cities[city.name!!] =
            city.toCity()
    }

    override fun onCityRemoved(
        city: FBCity
    ) {

        _cities.remove(
            city.name
        )
    }
}