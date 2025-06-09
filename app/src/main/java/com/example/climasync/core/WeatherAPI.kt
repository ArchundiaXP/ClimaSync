package com.example.climasync.core

import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.Days
import com.example.climasync.model.ForecastResponse
import com.example.climasync.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {
    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") coordinates: String
    ): Response<WeatherResponse>

    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") coordinates: String,
        @Query("days") days: Int
    ): Response<ForecastResponse>
}