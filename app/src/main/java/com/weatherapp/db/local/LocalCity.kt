package com.weatherapp.db.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.model.City

@Entity
data class LocalCity(

    @PrimaryKey
    var name: String,

    var latitude: Double,

    var longitude: Double,

    var isMonitored: Boolean
)

fun LocalCity.toCity() =

    City(
        name = name,
        location = LatLng(latitude, longitude),
        isMonitored = isMonitored
    )

fun City.toLocalCity() =

    LocalCity(
        name = name,
        latitude = location?.latitude ?: 0.0,
        longitude = location?.longitude ?: 0.0,
        isMonitored = isMonitored
    )