package com.example.weatherapp.navigation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(weatherViewModel: WeatherViewModel) {

    val coroutineScope = rememberCoroutineScope()


    Row(
        modifier = Modifier
            .fillMaxWidth() // This makes the Row occupy the entire width
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically // Aligns children vertically centered
    ) {
        Text(
            text = "WYBIERZ MIASTO",
            modifier = Modifier
//                .fillMaxWidth() // This makes the Text occupy the entire width within the Row
//                .align(Alignment.CenterHorizontally) // Aligns the Text horizontally centered within the Row
        )
        Button(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
//                    weatherViewModel.apiCallFull()
                }
            }

        }) {
            Text("api call")
        }
        when (val state = weatherViewModel.apiState.value) {
            is WeatherViewModel.ApiState.Loading -> {
                Text("Loading...")
            }
            is WeatherViewModel.ApiState.Success -> {
                Column {
                    Text("Timezone: ${weatherViewModel.timezone.value}")
                    Text("Temperature: ${weatherViewModel.temperature.value}")
                    Text("Address: ${weatherViewModel.address.value}")
                }
            }
            is WeatherViewModel.ApiState.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }




}