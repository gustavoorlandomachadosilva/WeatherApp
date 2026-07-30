package com.weatherapp.ui

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.weatherapp.R
import com.weatherapp.model.Weather
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val cities by viewModel.cities.collectAsState()

    val weatherMap by viewModel.weather.collectAsState(initial = emptyMap())

    val camPosState = rememberCameraPositionState()

    val context = LocalContext.current

    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),

        cameraPositionState = camPosState,

        onMapClick = {
            viewModel.addCity(it)
        },

        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        ),

        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true
        )

    ) {

        cities.values.forEach { city ->

            city.location?.let { location ->

                LaunchedEffect(city.name) {
                    viewModel.loadWeather(city.name)
                }

                val weather =
                    weatherMap[city.name]
                        ?: Weather.LOADING

                if (
                    weather != Weather.LOADING &&
                    weather != Weather.ERROR
                ) {
                    LaunchedEffect(weather) {
                        viewModel.loadBitmap(city.name)
                    }
                }

                val image =
                    weather.bitmap
                        ?: getDrawable(
                            context,
                            R.drawable.loading
                        )!!.toBitmap()

                val marker =
                    BitmapDescriptorFactory.fromBitmap(
                        image.scale(120, 120)
                    )

                Marker(
                    state = MarkerState(location),
                    icon = marker,
                    title = city.name,
                    snippet =
                        when (weather) {

                            Weather.LOADING ->
                                "Carregando clima..."

                            Weather.ERROR ->
                                "Erro ao carregar"

                            else ->
                                weather.desc
                        }
                )
            }
        }
    }
}