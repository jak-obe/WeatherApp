package com.example.weatherapp.navigation.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.weatherapp.Data.room.MiastoDao
import com.example.weatherapp.R
import com.example.weatherapp.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun MiastoScreen(
    miasto: String,
    userDao: MiastoDao,
    navController: NavController,
    weatherViewModel: WeatherViewModel
) {
    val coroutineScope = rememberCoroutineScope()


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

    fun navigateToNewMiasto(navController: NavController) {
        navController.navigate(route = "new_miasto_screen")
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
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {


        when (val state = weatherViewModel.apiState.value) {
            is WeatherViewModel.ApiState.Loading -> {
                Text("Loading...")
            }

            is WeatherViewModel.ApiState.Success -> {

                ItemRow(
                    label = miasto,
                    onDelete = deleteCurrentMiasto,
                    navigateToDefault = { navigateToDefault(navController) },
                    navigateToNewMiastoScreen = { navigateToNewMiasto(navController) }
                )


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    iconTempDesc(temperatura = weatherViewModel.temperature.value, desctipion =  weatherViewModel.description.value, icon = weatherViewModel.iconToday.value)

                    WeatherInfo(weatherViewModel.otherDaysList)
                }

                when (val stateHour = weatherViewModel.apiStateHourly.value) {

                    WeatherViewModel.ApiState.Loading -> {
                        Column() {
                            Text("Loading...")
                        }
                    }

                    is WeatherViewModel.ApiState.Success -> {
                        Column() {
                            // tutaj bedzie pasek z godzinami
                            WeatherHourlyInfoBar(weatherViewModel.hourlyTemperaturesVM)
                        }
                    }

                    is WeatherViewModel.ApiState.Error -> {
                        Text(text = "Error")
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


//@Preview(showBackground = true)
//@Composable
//fun WeatherHourlyInfoBarComposable() {
//    WeatherHourlyInfoBar()
//}


@Composable
fun WeatherHourlyInfoBar(tempAndIcon: MutableList<Pair<Double, String>>) {
    val lazyListState = rememberLazyListState()
    var initialIndex by remember { mutableStateOf(0) } // State to control initial item// Function to set the initial index
    fun setInitialIndex(index: Int) {
        initialIndex = index
    }

    fun getCurrentHour(): Int {
        val currentDateTime = LocalDateTime.now()
        Log.d("godzina", currentDateTime.hour.toString())
        return currentDateTime.hour
    }

    // in case ze tamto zle dziala
//    val rightNow = Calendar.getInstance()
//    val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY)

    // Launch an effect to scroll to the initial index when it changes
    LaunchedEffect(initialIndex) {
        lazyListState.scrollToItem(getCurrentHour())
    }

    val listaGodzin =
        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    val iconMap = mapOf(
        "clear-day" to R.drawable.baseline_sunny_24,
        "clear-night" to R.drawable.clearnight,
        "rain" to R.drawable.rain,
        "cloudy" to R.drawable.cloud,
        "partly-cloudy-day" to R.drawable.partlycloudy,
        "partly-cloudy-night" to R.drawable.partlycloudynight,
        // ... add more mappings
    )

    LazyRow(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
        items(24) { index -> // Adjust the count (10) as needed
            val (temperature, iconDescription) = tempAndIcon.getOrNull(index) ?: ("N/A" to "")
            val iconId = iconMap[iconDescription] ?: R.drawable.pending

            HourlyInfoItem(
                time = "${listaGodzin[index]}:00",
                temperature = "${temperature}°C",
                icon = painterResource(id = iconId)// Replace with actual icons
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
        Text(text = temperature)
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

    val iconMap = mapOf(
        "clear-day" to R.drawable.baseline_sunny_24,
        "clear-night" to R.drawable.clearnight,
        "rain" to R.drawable.rain,
        "cloudy" to R.drawable.cloud,
        "partly-cloudy-day" to R.drawable.partlycloudy,
        "partly-cloudy-night" to R.drawable.partlycloudynight,
        // ... add more mappings
    )

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
                    painter = painterResource(id = iconMap[icon] ?: R.drawable.pending),
                    contentDescription = "chmurka",
                )
                Column() {
                    Text(
                        text = "${dayOfWeek}",

                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "${dzien}",

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


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ItemRow(
    label: String,
    onDelete: (String) -> Unit,
    navigateToDefault: () -> Unit,
    navigateToNewMiastoScreen: () -> Unit
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


            IconButton(
                onClick = {
                    navigateToNewMiastoScreen()
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

@Composable
fun iconTempDesc(temperatura: String, desctipion: String, icon: String) {

    val iconMap = mapOf(
        "clear-day" to R.drawable.baseline_sunny_24,
        "clear-night" to R.drawable.clearnight,
        "rain" to R.drawable.rain,
        "cloudy" to R.drawable.cloud,
        "partly-cloudy-day" to R.drawable.partlycloudy,
        "partly-cloudy-night" to R.drawable.partlycloudynight,
        // ... add more mappings
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = iconMap[icon] ?: R.drawable.pending),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(64.dp) // Adjust size as needed
        )

        Text(
            "${temperatura}°C",
            fontSize = 56.sp,
            textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp)
        )

        Text(
            "${desctipion}",
            fontSize = 23.sp,
            textAlign = TextAlign.Center
        )
    }


}


