package com.weatherapp.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {

    private var weatherAPI: WeatherServiceAPI

    init {

        val retrofitAPI =
            Retrofit.Builder()
                .baseUrl(
                    WeatherServiceAPI.BASE_URL
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()

        weatherAPI =
            retrofitAPI.create(
                WeatherServiceAPI::class.java
            )
    }

    fun getName(
        lat: Double,
        lng: Double,
        onResponse: (String?) -> Unit
    ) {

        search("$lat,$lng") { loc ->

            onResponse(
                loc?.name
            )
        }
    }

    fun getForecast(
        name: String,
        onResponse:
            (APIWeatherForecast?) -> Unit
    ) {

        val call =
            weatherAPI.forecast(name)

        enqueue(call) {

            onResponse(it)
        }
    }

    fun getWeather(
        name: String,
        onResponse:
            (APICurrentWeather?) -> Unit
    ) {

        val call =
            weatherAPI.weather(name)

        enqueue(call) {

            onResponse(it)
        }
    }

    fun getLocation(
        name: String,
        onResponse: (
            lat: Double?,
            long: Double?
        ) -> Unit
    ) {

        search(name) { loc ->

            onResponse(
                loc?.lat,
                loc?.lon
            )
        }
    }

    private fun <T> enqueue(
        call: Call<T?>,
        onResponse: ((T?) -> Unit)? = null
    ) {

        call.enqueue(

            object :
                Callback<T?> {

                override fun onResponse(
                    call: Call<T?>,
                    response: Response<T?>
                ) {

                    onResponse?.invoke(
                        response.body()
                    )
                }

                override fun onFailure(
                    call: Call<T?>,
                    t: Throwable
                ) {

                    Log.w(
                        "WeatherApp WARNING",
                        t.message ?: ""
                    )
                }
            }
        )
    }

    private fun search(
        query: String,
        onResponse: (APILocation?) -> Unit
    ) {

        val call =
            weatherAPI.search(query)

        call.enqueue(

            object :
                Callback<List<APILocation>?> {

                override fun onResponse(
                    call: Call<List<APILocation>?>,
                    response: Response<List<APILocation>?>
                ) {

                    val result =
                        response.body()
                            ?.firstOrNull()

                    onResponse(result)
                }

                override fun onFailure(
                    call: Call<List<APILocation>?>,
                    t: Throwable
                ) {

                    Log.w(
                        "WeatherApp WARNING",
                        t.message ?: "Erro"
                    )

                    onResponse(null)
                }
            }
        )
    }
}