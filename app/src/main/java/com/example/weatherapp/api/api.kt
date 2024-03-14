package com.example.weatherapp.api

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherApi {


    @GET("timeline/{location}/tomorrow")
    suspend fun getWeatherTimeline(
        @Query("unitGroup") unitGroup: String,
        @Query("key") key: String,
        @Query("contentType") contentType: String,
        @Query("location") location: String
    ): JsonObject


    @GET("timeline/Suwa%C5%82ki?unitGroup=metric&include=days&key=P5WXCLMBC5KHHACPEMNLS76PP&contentType=json")
    suspend fun getcalyURL(
        @Query("unitGroup") unitGroup: String,
        @Query("key") key: String,
        @Query("contentType") contentType: String,
        @Query("location") location: String
    ): JsonObject


}




object obiektRetrofit {
    private const val BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/"
//    private const val BASE_URL = "http://api.weatherapi.com/v1/"

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