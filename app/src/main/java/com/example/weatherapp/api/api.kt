package com.example.weatherapp.api

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface weatherApi {


    @GET("timeline/{location}/tomorrow")
    suspend fun getWeatherTimeline(
        @Query("unitGroup") unitGroup: String,
        @Query("key") key: String,
        @Query("contentType") contentType: String,
        @Query("location") location: String
    ): JsonObject
}


object obiektRetrofit {
    private const val BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/"

    private val okHttpClient = OkHttpClient.Builder()
        .build()

    val api: weatherApi by lazy {
        try {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(weatherApi::class.java)
        } catch (e: Exception) {
            // Print the stack trace for debugging purposes
            e.printStackTrace()
            throw RuntimeException("Error initializing Retrofit: ${e.message}", e)
        }
    }
}