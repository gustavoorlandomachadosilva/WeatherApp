package com.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.api.WeatherService
import com.weatherapp.api.toForecast
import com.weatherapp.api.toWeather
import com.weatherapp.model.City
import com.weatherapp.model.Forecast
import com.weatherapp.model.User
import com.weatherapp.model.Weather
import com.weatherapp.monitor.ForecastMonitor
import com.weatherapp.repo.Repository
import com.weatherapp.ui.nav.Route

class MainViewModel(
    private val repository: Repository,
    private val service: WeatherService,
    private val forecastMonitor: ForecastMonitor
) : ViewModel(), Repository.Listener {

    private val _cities =
        mutableStateMapOf<String, City>()

    val cities
        get() =
            _cities.values
                .toList()
                .sortedBy { it.name }

    private val _user =
        mutableStateOf<User?>(null)

    private val _page =
        mutableStateOf<Route>(Route.Home)

    var page: Route
        get() = _page.value
        set(value) {
            _page.value = value
        }

    private val _weather =
        mutableStateMapOf<String, Weather>()

    private val _forecast =
        mutableStateMapOf<String, List<Forecast>?>()

    private val _city =
        mutableStateOf<String?>(null)

    var city: String?
        get() = _city.value
        set(value) {
            _city.value = value
        }

    val user: User?
        get() = _user.value

    init {
        repository.setListener(this)
    }

    fun remove(city: City) {
        repository.remove(city)
    }

    fun addCity(name: String) {

        service.getLocation(name) { lat, lng ->

            if (lat != null && lng != null) {

                repository.add(
                    City(
                        name = name,
                        location = LatLng(lat, lng)
                    )
                )
            }
        }
    }

    fun addCity(location: LatLng) {

        service.getName(
            location.latitude,
            location.longitude
        ) { name ->

            if (name != null) {

                repository.add(
                    City(
                        name = name,
                        location = location
                    )
                )
            }
        }
    }

    fun update(city: City) {

        repository.update(city)

        _cities[city.name] = city

        forecastMonitor.updateCity(city)
    }

    private fun loadWeather(name: String) {

        service.getWeather(name) { apiWeather ->

            apiWeather?.let {

                _weather[name] = it.toWeather()

                loadBitmap(name)
            }
        }
    }

    private fun loadBitmap(name: String) {

        _weather[name]?.let { weather ->

            service.getBitmap(weather.imgUrl) { bitmap ->

                _weather[name] =
                    weather.copy(bitmap = bitmap)
            }
        }
    }

    private fun loadForecast(name: String) {

        service.getForecast(name) {

            it?.let {

                _forecast[name] =
                    it.toForecast()
            }
        }
    }

    fun weather(name: String) =
        _weather.getOrPut(name) {

            loadWeather(name)

            Weather.LOADING
        }

    fun forecast(name: String) =
        _forecast.getOrPut(name) {

            loadForecast(name)

            emptyList()
        }

    override fun onUserLoaded(user: User) {

        _user.value = user
    }

    override fun onUserSignOut() {

        forecastMonitor.cancelAll()

        _cities.clear()
        _weather.clear()
        _forecast.clear()

        _city.value = null
        _page.value = Route.Home

        _user.value = null
    }

    override fun onCityAdded(city: City) {

        _cities[city.name] = city

        forecastMonitor.updateCity(city)
    }

    override fun onCityUpdated(city: City) {

        _cities[city.name] = city

        forecastMonitor.updateCity(city)
    }

    override fun onCityRemoved(city: City) {

        println("REMOVEU ${city.name}")

        _cities.remove(city.name)

        forecastMonitor.cancelCity(city)
    }
}