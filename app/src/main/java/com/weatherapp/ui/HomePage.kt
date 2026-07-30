package com.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.weatherapp.R
import com.weatherapp.model.Weather
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val weatherMap by viewModel.weather.collectAsStateWithLifecycle(emptyMap())
    val forecastMap by viewModel.forecast.collectAsStateWithLifecycle(emptyMap())
    val cities by viewModel.cities.collectAsStateWithLifecycle(emptyMap())

    val city = viewModel.city

    LaunchedEffect(city) {

        city?.let {

            viewModel.loadWeather(it)
            viewModel.loadForecast(it)
        }
    }

    Column {

        if (city == null) {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Blue)
                    .wrapContentSize(Alignment.Center)
            ) {

                Text(
                    text = "Selecione uma cidade!",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            }

        } else {

            val currentCity = cities[city]
            val weather =
                weatherMap[city] ?: Weather.LOADING

            LaunchedEffect(weather) {

                if (
                    weather != Weather.LOADING &&
                    weather != Weather.ERROR
                ) {

                    viewModel.loadBitmap(city)
                }
            }

            Row {

                AsyncImage(
                    model = weather.bitmap ?: weather.imgUrl,
                    contentDescription = "Imagem",
                    modifier = Modifier.size(140.dp),
                    error = painterResource(R.drawable.loading)
                )

                Column {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = city,
                            fontSize = 28.sp
                        )

                        currentCity?.let { c ->
                            IconButton(
                                onClick = {
                                    val updatedCity = c.copy(isMonitored = !c.isMonitored)
                                    viewModel.update(updatedCity)
                                }
                            ) {
                                Icon(
                                    imageVector = if (c.isMonitored) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                    contentDescription = "Monitorar"
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = when (weather) {

                            Weather.LOADING ->
                                "Carregando clima..."

                            Weather.ERROR ->
                                "Erro ao carregar"

                            else ->
                                weather.desc
                        },
                        fontSize = 22.sp
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = when (weather) {

                            Weather.LOADING ->
                                "Temp: ..."

                            Weather.ERROR ->
                                "Temp: ---"

                            else ->
                                "Temp: ${weather.temp}℃"
                        },
                        fontSize = 22.sp
                    )
                }
            }

            val forecasts =
                forecastMap[city] ?: emptyList()

            LazyColumn {

                items(forecasts) {

                    ForecastItem(
                        forecast = it,
                        onClick = {}
                    )
                }
            }
        }
    }
}