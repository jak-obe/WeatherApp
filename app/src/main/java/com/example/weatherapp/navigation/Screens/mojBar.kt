package com.example.weatherapp.navigation.Screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.example.weatherapp.Data.room.MiastoDao


@Composable
fun mojBarScreen(userDao: MiastoDao, navController: NavController) {
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

    mojBar(miastoList = miastoList, navController = navController)
}

@Composable
fun mojBar(miastoList: List<String>, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .border(1.dp, Color.DarkGray)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var showMenu by remember { mutableStateOf(false) }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                miastoList.forEach { miasto ->
                    pojedynczyNapis(miasto, navController = navController)
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
}

//TODO podkreślenie wybranego miasta.
// Wycentrować i powiększyć. Kiedyś zrobić zmiane miasta gestem w prawo/lewo.
// byćmoże zamiast plusa daj 3 kropki i po kliknieciu opcja dodania lub usniecia obecnego miasta :)


@Composable
fun pojedynczyNapis(miasto: String, navController: NavController) {
    var isSelected by remember { mutableStateOf(false) }

    Text(text = miasto,
        fontSize = 25.sp,
        style = TextStyle(
            textDecoration = if (isSelected) TextDecoration.Underline else null // Underline ONLY when selected
        ),
        modifier = Modifier.selectable(
            selected = isSelected,
            onClick = {
                isSelected = !isSelected
                navController.navigate(route = "miasto_screen/$miasto")
                Log.d("123", "klikniete")
            },
            role = Role.Tab
        )
    )
    Spacer(modifier = Modifier.width(8.dp))
}


//@Composable
//fun pojedynczyNapis(miasto: String, navController: NavController) {
//
//    Text(
//        text = miasto,
////        style = MaterialTheme.typography.labelMedium,
//        fontSize = 25.sp,
//        modifier = Modifier.selectable(
//            selected = false,
//            onClick = {
//                navController.navigate(route = "miasto_screen/$miasto") // Pass selected miasto as route argument
//                Log.d("123", "klikniete")
//            },
//            role = Role.Tab
//        )
//    )
//    Spacer(modifier = Modifier.width(8.dp))
//}

@Preview(showBackground = true)
@Composable
fun MojeBarPreview() {

    mojBar(
        miastoList = listOf("Warszawa", "Kraków", "Gdańsk"),
        navController = NavController(LocalContext.current)
    )
}
