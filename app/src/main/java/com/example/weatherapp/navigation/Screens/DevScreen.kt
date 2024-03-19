package com.example.weatherapp.navigation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DevScreen(){
    Column {
        Row {

            Button(onClick = {
//                lifecycleScope.launch {
//                    val response = mojviewModel.lecimy()
//                }
            }) {
                Text("VM")
            }


            Button(onClick = {
//                lifecycleScope.launch {
//                    val response = api.getcalyURL(
//                        key = key,
//                        contentType = "json",
//                        location = "Suwałki",
//                        unitGroup = "metric"
//                    )
//                    Log.d("123", "odpowiedz: ${response}")
//                }
            }) {
                Text("do loga")
            }



//            if (mojviewModel.timezone.value != "") {
//                Text("Miejsce: ${mojviewModel.address.value}")
//                Text("Temp: ${mojviewModel.temperature.value}")
//            }
        }

        Button(onClick = {
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO) {
//                    // Perform the database operation
//                    userDao.insertAll(Miasto(miasto = "WASILKÓW"))
//                }
//            }
        }) {
            Text(text = "dodaj miasto do db")
        }

        Button(onClick = {
//            userDao.getAll()
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO) {
//                    Log.d(
//                        "123", "miasta z db: ${userDao.getAll()}"
//                    )
//                }
//            }

        })
        {
            Text(text = "pobierz miasta z db")

        }


    }
}