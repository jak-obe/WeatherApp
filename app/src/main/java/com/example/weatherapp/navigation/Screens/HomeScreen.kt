package com.example.weatherapp.navigation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth() // This makes the Row occupy the entire width
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically // Aligns children vertically centered
    ) {
        Text(
            text = "WYBIERZ MIASTO",
            modifier = Modifier
                .fillMaxWidth() // This makes the Text occupy the entire width within the Row
//                .align(Alignment.CenterHorizontally) // Aligns the Text horizontally centered within the Row
        )
    }
}