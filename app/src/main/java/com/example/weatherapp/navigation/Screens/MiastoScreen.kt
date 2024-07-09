package com.example.weatherapp.navigation.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.weatherapp.Data.room.Database
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

    // TODO chujowo dziala ta lista pozioma, dodaj gest w prawo i lewo.

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
                weatherViewModel.apiCallHourly(desiredLocation = miasto)
            }
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {

        ItemRow(
            label = miasto,
            onDelete = deleteCurrentMiasto,
            navigateToDefault = { navigateToDefault(navController) },
            navController = navController
        )

        Spacer(modifier = Modifier.height(16.dp))



        when (val state = weatherViewModel.apiState.value) {
            is WeatherViewModel.ApiState.Loading -> {
                Text("Ładowanie...")
                Text(
                    "${weatherViewModel.temperature.value}°C",
                    fontSize = 56.sp,
                    textAlign = TextAlign.Center
                )
            }

            is WeatherViewModel.ApiState.Success -> {
                Column(modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {


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

                    Column(verticalArrangement = Arrangement.SpaceEvenly) {
                        WeatherInfo(weatherViewModel.otherDaysList)


                        // tutaj bedzie pasek z godzinami
                        WeatherHourlyInfoBar(weatherViewModel.hourlyTemperaturesVM)
                    }

                }
            }


            is WeatherViewModel.ApiState.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }
}


@Composable
fun WeatherInfo(otherDaysList: List<Triple<String, Double, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally), // Center the Column horizontally
        verticalArrangement = Arrangement.Center
    ) {
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
fun WeatherHourlyInfoBarComposable() {
//    WeatherHourlyInfoBar()
}


@Composable
fun WeatherHourlyInfoBar(listOfHourlyTemp:  List<Double>) {

//    val numbersArray = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14,15,16,17,18,19,20,21,22,23)

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(listOfHourlyTemp.size) { index -> // Adjust the count (10) as needed
            HourlyInfoItem(
                time = "${1}:00",
//                temperature = "${(15..30).random()}°C",
                temperature = "${listOfHourlyTemp[index]}°C",
                icon = painterResource(id = R.drawable.cloud)// Replace with actual icons
            )
        }
    }
}


@Composable
fun HourlyInfoItem(time: String, temperature: String, icon: Painter) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(80.dp), // Adjust width as needed
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = time)
        Spacer(modifier = Modifier.height(4.dp))
        Image(
            painter = icon,
            contentDescription = "Weather Icon",
            modifier = Modifier.size(32.dp) // Adjust size as needed
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = temperature.toString())
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
    var dayOfWeek = date.dayOfWeek.toString()
    dayOfWeek = dayOfWeek.substring(0, 3) + "."

    val imageId = when (icon) {
        "rain" -> R.drawable.rain
        "partly-cloudy-day" -> R.drawable.partlycloudy
        else -> R.drawable.baseline_sunny_24 // Optional default image
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black), modifier = Modifier
            .fillMaxWidth(0.9f) // Occupy 90% of screen width
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = "chmurka",
                )
                Column() {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${temp}",
                    modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                        .wrapContentHeight(Alignment.CenterVertically), // Center vertically within its container
                    textAlign = TextAlign.Center // Center text horizontally within the Text composable
                )
            }
            // TEMP
        }


    }
}


@Composable
fun ItemRow(
    label: String,
    onDelete: (String) -> Unit,
    navigateToDefault: () -> Unit,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = label)

        Row() {
            // ikona z buttonem do plusem do dadawania miasta
            IconButton(
                onClick = {
                    navController.navigate(route = "new_miasto_screen")
                },
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                    )
                }
            )

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
}

@Preview(showBackground = true)
@Composable
fun ItemRowPreview() {
    ItemRow(
        label = "Warsaw",
        onDelete = {},
        navigateToDefault = {},
        navController = rememberNavController()
    )
}
