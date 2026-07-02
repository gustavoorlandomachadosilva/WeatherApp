package com.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weatherapp.R
import com.weatherapp.model.Weather
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    Column {

        if (viewModel.city == null) {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Blue)
                    .wrapContentSize(
                        Alignment.Center
                    )
            ) {

                Text(
                    text = "Selecione uma cidade!",

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color.White,

                    textAlign =
                        TextAlign.Center,

                    fontSize = 28.sp,

                    modifier =
                        Modifier
                            .align(
                                Alignment.CenterHorizontally
                            )
                )
            }

        } else {

            Row {

                AsyncImage(
                    model = viewModel.weather(viewModel.city!!).imgUrl,
                    contentDescription = "Imagem",
                    modifier = Modifier.size(140.dp),
                    error = painterResource(R.drawable.loading)
                )

                Column {

                    Spacer(
                        Modifier.size(12.dp)
                    )

                    Text(
                        text =
                            viewModel.city
                                ?: "...",

                        fontSize =
                            28.sp
                    )

                    viewModel.city?.let { name ->

                        val weather =
                            viewModel.weather(
                                name
                            )

                        Spacer(
                            Modifier.size(
                                12.dp
                            )
                        )

                        Text(

                            text =
                                if (
                                    weather ==
                                    Weather.LOADING
                                )
                                    "Carregando clima..."
                                else
                                    weather.desc,

                            fontSize =
                                22.sp
                        )

                        Spacer(
                            Modifier.size(
                                12.dp
                            )
                        )

                        Text(

                            text =
                                if (
                                    weather ==
                                    Weather.LOADING
                                )
                                    "Temp: ..."

                                else

                                    "Temp: ${weather.temp}℃",

                            fontSize =
                                22.sp
                        )
                    }
                }
            }

            viewModel.city?.let { city ->

                val forecasts =
                    viewModel.forecast(city)

                LazyColumn {

                    items(
                        forecasts ?: emptyList()
                    ) {

                        ForecastItem(
                            forecast = it,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}