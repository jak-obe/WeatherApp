package com.example.weatherapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object DevScreen : Screen(route = "dev_screen")
    object NewMiastoScreen : Screen(route = "new_miasto_screen")
    object MiastoScreen : Screen(route = "miasto_screen")
}