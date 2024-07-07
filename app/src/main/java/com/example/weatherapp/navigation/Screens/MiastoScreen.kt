package com.example.weatherapp.navigation.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapp.Data.room.MiastoDao
import com.example.weatherapp.R
import com.example.weatherapp.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MiastoScreen(
    miasto: String,
    userDao: MiastoDao,
    navController: NavController,
    weatherViewModel: WeatherViewModel
) {


    val coroutineScope = rememberCoroutineScope()

    Log.d("ekran", "MiastoScreen: $miasto")

    val deleteCurrentMiasto: (String) -> Unit = { miasto ->
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val miastaList = userDao.getAll().firstOrNull()

                // Check if the list is not null and not empty
                miastaList?.let { miasta ->
                    // Find the first Miasto object with miasto value "876"
                    val miastoToDelete = miasta.firstOrNull { it.miasto == miasto.toString() }

                    // Check if the miastoToDelete is not null before deleting
                    miastoToDelete?.let { userDao.delete(it) }
                }
            }
        }
    }

    fun navigateToDefault(navController: NavController) {
        navController.navigate(route = "home_screen")
    }


    // Fetch weather data whenever miasto changes
    LaunchedEffect(miasto) {
        coroutineScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                weatherViewModel.apiCallFull(desiredLocation = miasto)
            }
        }
    }





    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        ItemRow(
            label = miasto,
            onDelete = deleteCurrentMiasto,
            navigateToDefault = { navigateToDefault(navController) }
        )


        when (val state = weatherViewModel.apiState.value) {
            is WeatherViewModel.ApiState.Loading -> {
                Text("Loading...")
            }

            is WeatherViewModel.ApiState.Success -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Text(
                        "${weatherViewModel.temperature.value}°C",
                        fontSize = 56.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "${weatherViewModel.description.value}",
                        fontSize = 23.sp,
                        textAlign = TextAlign.Center
                    )
                    WeatherInfo(weatherViewModel.otherDaysList)
                }
            }

            is WeatherViewModel.ApiState.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }


}


// Tabela z dniami wrapper
@Composable
fun WeatherInfo(otherDaysList: List<Triple<String, Double, String>>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .width(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(otherDaysList) { index, (day, temperature, icon) ->
                DaysTableItem(dzien = day, temp = "${temperature}°C", icon = icon)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherInfo() {
    WeatherInfo(
        listOf(
            Triple("2024-07-07", 12.0, "rainy"),
            Triple("2024-07-08", 13.0, "rainy"),
            Triple("2024-07-09", 14.0, "rainy"),
            Triple("2024-07-10", 15.0, "rainy"),
            Triple("2024-07-11", 16.0, "rainy"),
            Triple("2024-07-12", 17.0, "rainy"),
        )
    )
}

@Composable
fun DaysTableItem(dzien: String, temp: String, icon: String) {

    val formatter = DateTimeFormatter.ISO_DATE
    val date = LocalDate.parse(dzien, formatter)
    val dayOfWeek = date.dayOfWeek

    val imageId = when (icon) {
        "rain" -> R.drawable.rain
        "partly-cloudy-day" -> R.drawable.partlycloudy
        else-> R.drawable.baseline_sunny_24 // Optional default image
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .size(width = 2600.dp, height = 75.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row() {


                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = "chmurka",
                )

                Column {
                    Text(
                        text = "${dayOfWeek}",
//                    modifier = Modifier
//                        .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "${dzien}",
//                    modifier = Modifier
//                        .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // TEMP
            Text(
                text = "${temp}",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }


    }
}


@Composable
fun ItemRow(label: String, onDelete: (String) -> Unit, navigateToDefault: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = label)

        IconButton(
            onClick = {
                onDelete(label)
                navigateToDefault()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete", tint = Color.Red
            )
        }
    }
}