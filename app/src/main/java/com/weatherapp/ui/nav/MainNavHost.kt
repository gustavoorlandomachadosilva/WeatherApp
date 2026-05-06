package com.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weatherapp.ui.HomePage
import com.weatherapp.ui.ListPage
import com.weatherapp.ui.MapPage

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.toString()
    ) {

        composable(Route.Home.toString()) {
            HomePage(modifier = modifier)
        }

        composable(Route.List.toString()) {
            ListPage(modifier = modifier)
        }

        composable(Route.Map.toString()) {
            MapPage(modifier = modifier)
        }
    }
}