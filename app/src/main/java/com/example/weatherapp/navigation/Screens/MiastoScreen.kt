package com.example.weatherapp.navigation.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.weatherapp.Data.room.Miasto
import com.example.weatherapp.Data.room.MiastoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MiastoScreen(miasto: String, userDao: MiastoDao, navController: NavController) {

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




    ItemRow(
        label = miasto,
        onDelete = deleteCurrentMiasto,
        navigateToDefault = { navigateToDefault(navController) }
    )
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
                contentDescription = "Delete"
            )
        }
    }
}