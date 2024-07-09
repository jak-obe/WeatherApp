package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.weatherapp.Data.room.Database
import com.example.weatherapp.Data.room.Miasto
import com.example.weatherapp.navigation.Screen
import com.example.weatherapp.navigation.Screens.DevScreen
import com.example.weatherapp.navigation.Screens.HomeScreen
import com.example.weatherapp.navigation.Screens.MiastoScreen
import com.example.weatherapp.navigation.Screens.NewMiastoScreen
import com.example.weatherapp.navigation.Screens.mojBarScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {


        val key = "P5WXCLMBC5KHHACPEMNLS76PP"


        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "mojdatabase"
        ).build()
        val userDao = db.userDao()




        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val firstObject = userDao.getFirstMiasto()
                if (firstObject != null) {
                    Log.d("10", firstObject.miasto.toString())
                }
            }
        }


        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {

                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var startDestination by remember { mutableStateOf<String?>(null) }
                    startDestination = Screen.Home.route
                    var firstMiasto by remember { mutableStateOf<String?>(null) } // Store first city

                    LaunchedEffect(key1 = Unit) { // Trigger coroutine once
                        val firstObject = userDao.getFirstMiasto()
                        if (firstObject != null) {
                            Log.d("123", "gowno")
                            firstObject.miasto?.let {
                                startDestination = Screen.MiastoScreen.route + "/${it}"
                                firstMiasto = firstObject.miasto
                            }
                        }
                    }


                    Scaffold(
                        topBar = {
                            mojBarScreen(userDao = userDao, navController = navController)
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {

                            val mojviewModel = WeatherViewModel()

                            composable(route = Screen.Home.route) {
                                HomeScreen(
                                    weatherViewModel = mojviewModel,
                                    navController = navController,
                                    userDao = userDao
                                )
                            }
                            composable(route = Screen.NewMiastoScreen.route) {
                                NewMiastoScreen(userDao = userDao, navController = navController)
                            }
                            composable(route = Screen.MiastoScreen.route + "/{miasto}") { // Define route with argument
                                val miasto = it.arguments?.getString("miasto") ?: ""
                                MiastoScreen(
                                    miasto = miasto,
                                    userDao = userDao,
                                    navController = navController,
                                    weatherViewModel = mojviewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val CITY_ID = "Białystok"
    }
}