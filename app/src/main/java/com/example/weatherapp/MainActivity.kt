package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.weatherapp.Data.room.Database
import com.example.weatherapp.Data.room.Miasto
import com.example.weatherapp.api.obiektRetrofit.api
import com.example.weatherapp.navigation.Screen
import com.example.weatherapp.navigation.Screens.DevScreen
import com.example.weatherapp.navigation.Screens.HomeScreen
import com.example.weatherapp.navigation.Screens.MiastoScreen
import com.example.weatherapp.navigation.Screens.NewMiastoScreen
import com.example.weatherapp.navigation.Screens.mojBar
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {


        val mojviewModel = WeatherViewModel()
        val key = "P5WXCLMBC5KHHACPEMNLS76PP"


        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "mojdatabase"
        ).build()
        val userDao = db.userDao()



        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // Retrieve the list of Miasto objects
                val miastaList = userDao.getAll().firstOrNull()

                // Check if the list is not null and not empty
                miastaList?.let { miasta ->
                    // Find the first Miasto object with miasto value "876"
                    val miastoToDelete = miasta.firstOrNull { it.miasto == "marek" }

                    // Check if the miastoToDelete is not null before deleting
                    miastoToDelete?.let { userDao.delete(it) }
                }
            }
        }



        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {

                val navController = rememberNavController()
//                val hasSavedCities = remember { mutableStateOf(false) }

                val coroutineScope = rememberCoroutineScope()
//                val hasSavedCities = coroutineScope.launch {
//                    withContext(Dispatchers.IO) {
//                        userDao.getAll().firstOrNull()?.isNotEmpty() ?: false
//                    }
//                }
                coroutineScope.launch {
                    val hasSavedCities = userDao.getAll()
                    //TODO zrobić tak aby strona startowa to był pierwszy miasto z listy
                }
                val hasSavedCities = userDao.getAll().map { it.isNotEmpty() }

                val firstMiasto = userDao.getAll().map { it.firstOrNull()?.miasto ?: "" }




                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    //TODO sproboj tego czegos z kursu, db ma

                    Scaffold(
                        topBar = {
                            mojBar(userDao = userDao, navController = navController)
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = Screen.Home.route) {
                                HomeScreen(mojviewModel)
                            }
                            composable(route = Screen.DevScreen.route) {
                                DevScreen()
                            }
                            composable(route = Screen.NewMiastoScreen.route) {
                                NewMiastoScreen(userDao)
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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}

//@Composable
//fun apkaSoFar(){
//    Column {
//        Row {
//
//            Button(onClick = {
//                lifecycleScope.launch {
//                    val response = mojviewModel.lecimy()
//                }
//            }) {
//                Text("VM")
//            }
//
//
//            Button(onClick = {
//                lifecycleScope.launch {
//                    val response = api.getcalyURL(
//                        key = key,
//                        contentType = "json",
//                        location = "Suwałki",
//                        unitGroup = "metric"
//                    )
//                    Log.d("123", "odpowiedz: ${response}")
//                }
//            }) {
//                Text("do loga")
//            }
//
//
//
//            if (mojviewModel.timezone.value != "") {
//                Text("Miejsce: ${mojviewModel.address.value}")
//                Text("Temp: ${mojviewModel.temperature.value}")
//            }
//        }
//
//        Button(onClick = {
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO) {
//                    // Perform the database operation
//                    userDao.insertAll(Miasto(miasto = "WASILKÓW"))
//                }
//            }
//        }) {
//            Text(text = "dodaj miasto do db")
//        }
//
//        Button(onClick = {
//            userDao.getAll()
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO) {
//                    Log.d(
//                        "123", "miasta z db: ${userDao.getAll()}"
//                    )
//                }
//            }
//
//        })
//        {
//            Text(text = "pobierz miasta z db")
//
//        }
//
//
//    }
//}