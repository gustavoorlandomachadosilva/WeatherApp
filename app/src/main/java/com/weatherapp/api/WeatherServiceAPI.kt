package com.weatherapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceAPI {

    companion object {
        const val BASE_URL =
            "https://api.weatherapi.com/v1/"
    }

    @GET("search.json")
    fun search(
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("lang") lang: String = "pt"
    ): Call<List<APILocation>?>

    @GET("current.json")
    fun weather(
        @Query("q") city: String,
        @Query("key") key: String,
        @Query("lang") lang: String = "pt"
    ): Call<APICurrentWeather?>

    @GET("forecast.json")
    fun forecast(
        @Query("q") city: String,
        @Query("key") key: String,
        @Query("lang") lang: String = "pt",
        @Query("days") days: Int = 7
    ): Call<APIWeatherForecast?>
}