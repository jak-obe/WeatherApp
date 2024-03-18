package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.weatherapp.Data.room.Database
import com.example.weatherapp.Data.room.Miasto
import com.example.weatherapp.api.obiektRetrofit.api
import com.example.weatherapp.navigation.Screens.ScaffoldExample
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {


        lateinit var navController: NavHostController


        val mojviewModel = WeatherViewModel()
        val key = "P5WXCLMBC5KHHACPEMNLS76PP"


        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "mojdatabase"
        ).build()
        val userDao = db.userDao()

        lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // Perform the database operation
                    userDao.insertAll(Miasto(miasto = "RADZYMIN"))
                }
            }

        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    ScaffoldExample(userDao = userDao)


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