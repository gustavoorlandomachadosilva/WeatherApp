package com.weatherapp.api

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService(
    private val context: Context
) {

    private var weatherAPI: WeatherServiceAPI

    private val imageLoader =
        ImageLoader.Builder(context)
            .allowHardware(false)
            .build()

    init {

        val retrofitAPI =
            Retrofit.Builder()
                .baseUrl(WeatherServiceAPI.BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()

        weatherAPI =
            retrofitAPI.create(
                WeatherServiceAPI::class.java
            )
    }

    /**
     * Busca o nome da cidade pelas coordenadas.
     */
    suspend fun getName(
        lat: Double,
        lng: Double
    ): String? =
        withContext(Dispatchers.IO) {

            search("$lat,$lng")?.name
        }


    /**
     * Busca as coordenadas da cidade pelo nome.
     */
    suspend fun getLocation(
        name: String
    ): LatLng? =
        withContext(Dispatchers.IO) {

            val location = search(name)
            val lat = location?.lat
            val lon = location?.lon

            if (lat != null && lon != null) {

                LatLng(
                    lat,
                    lon
                )

            } else {

                null
            }
        }

    /**
     * Função utilizada pelas duas pesquisas acima.
     */
    private fun search(
        query: String
    ): APILocation? {

        val call: Call<List<APILocation>?> =
            weatherAPI.search(
                query,
                BuildConfig.WEATHER_API_KEY
            )

        val result =
            call.execute().body()

        return if (!result.isNullOrEmpty())
            result[0]
        else
            null
    }

    /**
     * Clima atual.
     */
    suspend fun getWeather(
        name: String
    ): APICurrentWeather? =
        withContext(Dispatchers.IO) {

            val call: Call<APICurrentWeather?> =
                weatherAPI.weather(
                    name,
                    BuildConfig.WEATHER_API_KEY
                )

            call.execute().body()
        }

    /**
     * Previsão do tempo.
     */
    suspend fun getForecast(
        name: String
    ): APIWeatherForecast? =
        withContext(Dispatchers.IO) {

            val call: Call<APIWeatherForecast?> =
                weatherAPI.forecast(
                    name,
                    BuildConfig.WEATHER_API_KEY
                )

            call.execute().body()
        }

    /**
     * Baixa o ícone do clima.
     */
    suspend fun getBitmap(
        imgUrl: String
    ): Bitmap? =
        withContext(Dispatchers.IO) {

            val request =
                ImageRequest.Builder(context)
                    .data(imgUrl)
                    .allowHardware(false)
                    .build()

            val response =
                imageLoader.execute(request)

            response.drawable?.toBitmap()
        }
}