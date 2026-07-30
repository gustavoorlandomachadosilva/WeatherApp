package com.weatherapp.ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun BottomNavBar(
    viewModel: MainViewModel,
    items: List<BottomNavItem>
) {

    NavigationBar(
        contentColor = Color.Black
    ) {

        items.forEach { item ->

            NavigationBarItem(

                selected = viewModel.page == item.route,

                onClick = {
                    viewModel.page = item.route
                },

                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },

                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp
                    )
                },

                alwaysShowLabel = true
            )
        }
    }
}