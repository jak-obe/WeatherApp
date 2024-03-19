package com.example.weatherapp.navigation.Screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.Data.room.MiastoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



@Composable
fun mojBar(userDao: MiastoDao, navController: NavController) {
    var miastoList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val users = userDao.getAll()
            miastoList = users.filterNotNull().map { it.miasto.toString() }
        }
    }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .border(1.dp, Color.DarkGray),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(
            text = "xyz",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.selectable(
                selected = false,
                onClick = { Log.d("123", "klikniete")
                          navController.navigate(route="dev_screen")},
                role = Role.Tab
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row() {
            miastoList.forEach { miasto ->
                pojedynczyNapis(miasto)
            }


            // na koncu plusik do dodawania miasta
            IconButton(
                onClick = {},
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
        }
    }
}


@Composable
fun pojedynczyNapis(miasto: String) {
    Text(
        text = miasto,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.selectable(
            selected = false,
            onClick = { Log.d("123", "klikniete") },
            role = Role.Tab
        )
    )
    Spacer(modifier = Modifier.width(8.dp))
}

@Preview
@Composable
fun prewju() {

}
