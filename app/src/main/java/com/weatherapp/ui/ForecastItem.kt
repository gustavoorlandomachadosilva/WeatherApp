package com.weatherapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.model.Forecast
import java.text.DecimalFormat

@Composable
fun ForecastItem(
    forecast: Forecast,
    modifier: Modifier = Modifier,
    onClick: (Forecast) -> Unit
) {

    val format =
        DecimalFormat("#.0")

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable {
                    onClick(forecast)
                },

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            Icons.Default.LocationOn,
            null
        )

        Spacer(
            Modifier.size(16.dp)
        )

        Column {

            Text(
                forecast.weather,
                fontSize = 24.sp
            )

            Text(
                forecast.date
            )

            Text(
                "Min: ${
                    format.format(
                        forecast.tempMin
                    )
                }℃"
            )

            Text(
                "Max: ${
                    format.format(
                        forecast.tempMax
                    )
                }℃"
            )
        }
    }
}