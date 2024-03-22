package com.example.weatherapp.navigation.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapp.Data.room.MiastoDao
import com.example.weatherapp.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MiastoScreen(miasto: String, userDao: MiastoDao, navController: NavController,weatherViewModel : WeatherViewModel) {

    //TODO
    // wsadz tutaj api i zrób tak aby robiło VM robił call przy odpaleniu tego screena
    // zrob odpowiednie "kontenery" na odpowiednie dane ktore beda przechwytywane
    // najpierw zrob frontend :)))))

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


    // Fetch weather data whenever miasto changes
    LaunchedEffect(miasto) {
        coroutineScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                weatherViewModel.apiCallFull(desiredLocation = miasto)
            }
        }
    }





    Column {

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
                Column {


//                    Text("Timezone: ${weatherViewModel.timezone.value}")
                    Text("${weatherViewModel.temperature.value}", fontSize = 56.sp, textAlign = TextAlign.Center)
//                    Text("Address: ${weatherViewModel.address.value}")
//                    Text("asd: ${miasto.toString()}")
                    WeatherInfo(weatherViewModel.otherDaysList)
                }
            }
            is WeatherViewModel.ApiState.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }


}



@Composable
fun WeatherInfo(otherDaysList: List<Pair<String, Double>>){


    Column {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            itemsIndexed(otherDaysList) { index, (day, temperature) ->
                DaysTableItem(dzien = day, temp = "${temperature}°C")
            }
        }

    }

}

@Composable
fun DaysTableItem(dzien:String, temp: String){
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .size(width = 240.dp, height = 50.dp)
    ) {
        Row(){

            // DZIEŃ
            Text(
                text = "${dzien}",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
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
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Color.Black
        )
        IconButton(
            onClick = {
                onDelete(label)
                navigateToDefault()
            }
        ) {
            // You can replace Icons.Default.Delete with any other icon
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete", tint = Color.Red
            )
        }
    }
}