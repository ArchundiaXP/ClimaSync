package com.example.climasync.core

import com.example.climasync.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {
    @GET("/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): Response<WeatherResponse>
}