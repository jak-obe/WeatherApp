package com.example.weatherapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.weatherapp.api.obiektRetrofit
import com.google.gson.JsonObject
import org.json.JSONObject
import java.net.URLDecoder
import java.io.UnsupportedEncodingException

class WeatherViewModel : ViewModel() {

    val currentLocation = mutableStateOf("")

    val weatherApiObject = obiektRetrofit.api

    val description = mutableStateOf("")
    val timezone = mutableStateOf("")
    val temperature = mutableStateOf("")
    val address = mutableStateOf("")
    val apiState = mutableStateOf<ApiState>(ApiState.Loading)
    val otherDaysList = mutableListOf<Triple<String, Double, String>>()


    val key = "P5WXCLMBC5KHHACPEMNLS76PP"

    suspend fun apiCallFull(desiredLocation: String) {

        val decodedLocation = URLDecoder.decode(desiredLocation, "UTF-8")

        val result: Result<JsonObject> = runCatching {
            weatherApiObject.getcalyURL(
                key = key,
                contentType = "json",
                location = decodedLocation,
                unitGroup = "metric"
            )
        }

        result.onSuccess { response ->
            // Handle successful response
            parserodpowiedzi(response.toString()) // Use the response directly
            apiState.value = ApiState.Success()
            Log.d("123", "odpowiedz poprawna: ${response}")
        }

        result.onFailure { exception ->
            // Handle error
            apiState.value = ApiState.Error(exception.message ?: "Unknown error")
            Log.d("123", "Error occurred: ${exception.message}")
        }
    }

    // wazne: description - przewidywana pogoda w skrocie
    // jako temperature bierz tempmax

    fun parserodpowiedzi(response: String) {
        val jsonObject = JSONObject(response)
        timezone.value = jsonObject.getString("timezone")
        address.value = jsonObject.getString("address")
//        description.value = jsonObject.getString("description")

        val dayArray = jsonObject.getJSONArray("days")

        if (dayArray.length() > 0) {
            val currentDayObject = dayArray.getJSONObject(0)
            val currentDayTemp = currentDayObject.getDouble("tempmax")
            val currentDayText = currentDayObject.getString("conditions")
            temperature.value = currentDayTemp.toString()
            description.value = currentDayText

//            val otherDaysList = mutableListOf<Pair<String, Double>>()
            otherDaysList.clear()
            for (i in 1 until minOf(
                dayArray.length(),
                7
            )) { // Ensure we don't exceed the length of dayArray or 5
                val dayObject = dayArray.getJSONObject(i)
                val date = dayObject.getString("datetime")
                val temp = dayObject.getDouble("tempmax")
                val icon = dayObject.getString("icon")
                otherDaysList.add(Triple(date, temp, icon))
            }

            Log.d("123", otherDaysList.toString())

            // Now otherDaysList contains pairs of date and temperature for each day except the current day
            // You can use this list as needed
        } else {
            // Handle the case when there are no days in the response
        }

    }

    sealed class ApiState {
        object Loading : ApiState()
        data class Success(val data: Any? = null) : ApiState()
        data class Error(val message: String) : ApiState()
    }
}
