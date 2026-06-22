package com.weatherapp.ui

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

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

        onMapClick = { position ->

            viewModel.addCity(position)

        },

        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        ),

        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true
        )

    ) {

        viewModel.cities.forEach { city ->

            city.location?.let { location ->

                Marker(
                    state = MarkerState(
                        position = location
                    ),

                    title = city.name,

                    snippet =
                        "${location.latitude}, ${location.longitude}"
                )
            }
        }
    }
}