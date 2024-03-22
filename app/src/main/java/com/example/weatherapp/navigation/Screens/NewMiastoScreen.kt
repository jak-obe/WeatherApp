package com.example.weatherapp.navigation.Screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.Data.room.Miasto
import com.example.weatherapp.Data.room.MiastoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NewMiastoScreen(userDao: MiastoDao) {

    val coroutineScope = rememberCoroutineScope()

    val dodajNoweMiasto: (String) -> Unit = { miasto ->
        coroutineScope.launch {
            withContext(Dispatchers.IO){
                userDao.insertAll(Miasto(miasto = miasto))
            }
        }
    }


    SimpleFormWithButton {
        dodajNoweMiasto(it)
    }
}


@Composable
fun NewMiasto() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFormWithButton(onButtonClick: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            placeholder = { Text("Dodaj nowe miasto!") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Black) // Add black border
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onButtonClick(textFieldValue)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun priwjuForm() {
    SimpleFormWithButton({})
}