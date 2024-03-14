package com.example.weatherapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherapp.api.obiektRetrofit
import com.example.weatherapp.api.obiektRetrofit.api
import com.google.gson.JsonObject
import org.json.JSONObject

class WeatherViewModel : ViewModel() {

    val weatherApiObject = obiektRetrofit.api

    val timezone = mutableStateOf("")
    val temperature = mutableStateOf("")
    val address = mutableStateOf("")

    val key = "P5WXCLMBC5KHHACPEMNLS76PP"

    suspend fun lecimy() {
        val result: Result<JsonObject> = runCatching {
            weatherApiObject.getcalyURL(
                key = key,
                contentType = "json",
                location = "Suwałki",
                unitGroup = "metric"
            )
        }

        result.onSuccess { response ->
            // Handle successful response
            parserodpowiedzi(response.toString()) // Use the response directly
            Log.d("123", "odpowiedz poprawna")
        }

        result.onFailure { exception ->
            // Handle error
            Log.d("123", "Error occurred: ${exception.message}")
        }
    }

    // wazne: description - przewidywana pogoda w skrocie

    fun parserodpowiedzi(response: String){
        val jsonObject = JSONObject(response)
        timezone.value = jsonObject.getString("timezone")
        address.value = jsonObject.getString("address")

        val dayArray = jsonObject.getJSONArray("days")

        if (dayArray.length() > 0) {
            val firstDayObject = dayArray.getJSONObject(0)
            val temp = firstDayObject.getDouble("temp")
            temperature.value = temp.toString()
        } else {
            // Handle the case when there are no days in the response
        }

//        val daysArray = jsonObject.getJSONArray("days")

//        temperature.value = daysArray.getString(4)



    }

}