package com.example.weatherapp.navigation

sealed class Screen(val route:String) {
    object Home: Screen(route = "home_screen")
    object tempScreen: Screen(route = "temp_screen")
}