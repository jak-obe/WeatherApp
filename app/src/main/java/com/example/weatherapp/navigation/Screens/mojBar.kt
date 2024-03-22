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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.example.weatherapp.Data.room.MiastoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



@Composable
fun mojBar(userDao: MiastoDao, navController: NavController) {
    var miastoList by remember { mutableStateOf<List<String>>(emptyList()) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

    val coroutineScope = rememberCoroutineScope()
    val userDaoFlow = userDao.getAll()

    LaunchedEffect(context, lifecycleOwner, savedStateRegistryOwner) {
        userDaoFlow.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { users ->
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
                onClick = {
                    // Handle click action
                    navController.navigate(route = "dev_screen")
                },
                role = Role.Tab
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row() {
            miastoList.forEach { miasto ->
                pojedynczyNapis(miasto, navController =  navController)
            }

            // na koncu plusik do dodawania miasta
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
        }
    }
}

//TODO pomysl jak nawigować do pojedyńczego miasta
// Zrób jeden ekran dla wszystkich miast
// musi tam byc mozliwosc podgladu pogody i usuwania danego miasta !!!!
@Composable
fun pojedynczyNapis(miasto: String,navController: NavController) {
    Text(
        text = miasto,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.selectable(
            selected = false,
            onClick = {
                navController.navigate(route = "miasto_screen/$miasto") // Pass selected miasto as route argument
                Log.d("123", "klikniete") },
            role = Role.Tab
        )
    )
    Spacer(modifier = Modifier.width(8.dp))
}

@Preview
@Composable
fun prewju() {

}
