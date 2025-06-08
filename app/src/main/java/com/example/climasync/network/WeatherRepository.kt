package com.example.climasync.network

import android.util.Log
import com.example.climasync.core.RetrofitInstance
import com.example.climasync.core.WeatherAPI
import com.example.climasync.model.WeatherResponse

class WeatherRepository {
    private val retrofit = RetrofitInstance.getRetrofit().create(WeatherAPI::class.java)

    suspend fun getCurrentWeather(coordinates: String): WeatherResponse? {
        return try {
            val response = retrofit.getCurrentWeather(
                apiKey ="2c3b876526d649368b8143309250606",
                coordinates
            )
            Log.d("WEATHER_API", "Datos obtenidos: ${response.body()}")
            response.body()
        } catch (e: Exception) {
            Log.e("WEATHER_ERROR", "Error en la solicitud: ${e.message}")
            null
        }
    }
}