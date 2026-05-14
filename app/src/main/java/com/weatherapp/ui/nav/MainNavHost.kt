package com.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weatherapp.ui.HomePage
import com.weatherapp.ui.ListPage
import com.weatherapp.ui.MapPage
import com.weatherapp.viewmodel.MainViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Route.Home.toString()
    ) {

        composable(Route.Home.toString()) {
            HomePage(
                modifier = modifier,
                viewModel = viewModel
            )
        }

        composable(Route.List.toString()) {
            ListPage(
                modifier = modifier,
                viewModel = viewModel
            )
        }

        composable(Route.Map.toString()) {
            MapPage(
                modifier = modifier,
                viewModel = viewModel
            )
        }
    }
}