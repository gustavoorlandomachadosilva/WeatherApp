package com.weatherapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weatherapp.R
import com.weatherapp.model.City
import com.weatherapp.model.Weather
import com.weatherapp.ui.nav.Route
import com.weatherapp.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CityItem(
    city: City,
    weather: Weather,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {

    val desc =
        if (weather == Weather.LOADING)
            "Carregando clima..."
        else
            weather.desc

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },

        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = weather.imgUrl,
            contentDescription = "Imagem",
            modifier = Modifier.size(75.dp),
            error = painterResource(R.drawable.loading)
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = city.name,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    imageVector =
                        if (city.isMonitored)
                            Icons.Filled.Notifications
                        else
                            Icons.Outlined.Notifications,

                    contentDescription = "Monitorada",

                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = desc,
                fontSize = 16.sp
            )
        }

        IconButton(
            onClick = onClose
        ) {

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remover"
            )
        }
    }
}

@Composable
fun ListPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val cities by viewModel.cities.collectAsStateWithLifecycle(emptyMap())
    val weatherMap by viewModel.weather.collectAsStateWithLifecycle(emptyMap())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        items(
            items = cities.values.toList().sortedBy { it.name },
            key = { it.name }
        ) { city ->

            LaunchedEffect(city.name) {
                viewModel.loadWeather(city.name)
            }

            val weather = weatherMap[city.name] ?: Weather.LOADING

            CityItem(

                city = city,

                weather = weather,

                onClose = {
                    viewModel.remove(city)
                },

                onClick = {

                    viewModel.city = city.name
                    viewModel.page = Route.Home
                }
            )
        }
    }
}