package com.example.weatherapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object DevScreen : Screen(route = "dev_screen")
}