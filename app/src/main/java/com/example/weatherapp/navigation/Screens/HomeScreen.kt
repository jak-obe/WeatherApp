package com.example.weatherapp.navigation.Screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.weatherapp.Data.room.MiastoDao
import com.example.weatherapp.WeatherViewModel
import com.example.weatherapp.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope

@Composable
fun HomeScreen(
    weatherViewModel: WeatherViewModel,
    navController: NavController,
    userDao: MiastoDao
) {

    val coroutineScope = rememberCoroutineScope()

    var firstMiasto by remember { mutableStateOf<String?>(null) } // Store first city


    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val firstObject = userDao.getFirstMiasto()

            withContext(Dispatchers.Main) {
                if (firstObject != null) {
                    Log.d("10", firstObject.miasto.toString())
                    firstMiasto = firstObject.miasto.toString()
                } else {
                    Log.d("10", "jest null")
                }

                if (firstMiasto != null) {
                    Log.d("nawigacja", "nie jest null")
                    navController.navigate("${Screen.MiastoScreen.route}/${firstMiasto}")
                } else {
                    Log.d("nawigacja", "jest null")
                    navController.navigate(Screen.NewMiastoScreen.route)
                }
            }
        }
    }


//    LaunchedEffect(key1 = Unit) {
//        coroutineScope.launch(Dispatchers.IO) {
//            val firstObject = userDao.getFirstMiasto()
//            if (firstObject != null) {
//                Log.d("10", firstObject.miasto.toString())
//                firstMiasto = firstObject.miasto.toString()
//            }
//            else
//            {
//                Log.d("10", "jest null")
//            }
//        }
//    }
//
//
//
//    if (firstMiasto != null) {
//        Log.d("nawigacja","nie jest null")
//        navController.navigate("${Screen.MiastoScreen.route}/${firstMiasto}")
//    } else {
//        Log.d("nawigacja","jest")
//        navController.navigate(Screen.NewMiastoScreen.route)
//    }


//    Row(
//        modifier = Modifier
//            .fillMaxWidth() // This makes the Row occupy the entire width
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically // Aligns children vertically centered
//    ) {
//        Text(
//            text = "WYBIERZ MIASTO",
//            modifier = Modifier
////                .fillMaxWidth() // This makes the Text occupy the entire width within the Row
////                .align(Alignment.CenterHorizontally) // Aligns the Text horizontally centered within the Row
//        )
//        Button(onClick = {
//            coroutineScope.launch(Dispatchers.IO) {
//                withContext(Dispatchers.IO) {
////                    weatherViewModel.apiCallFull()
//                }
//            }
//
//        }) {
//            Text("api call")
//        }
//        when (val state = weatherViewModel.apiState.value) {
//            is WeatherViewModel.ApiState.Loading -> {
//                Text("Loading...")
//            }
//
//            is WeatherViewModel.ApiState.Success -> {
//                Column {
//                    Text("Timezone: ${weatherViewModel.timezone.value}")
//                    Text("Temperature: ${weatherViewModel.temperature.value}")
//                    Text("Address: ${weatherViewModel.address.value}")
//                }
//            }
//
//            is WeatherViewModel.ApiState.Error -> {
//                Text("Error: ${state.message}")
//            }
//        }
//    }
}