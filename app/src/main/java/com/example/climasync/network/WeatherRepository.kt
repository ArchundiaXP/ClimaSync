package com.example.climasync.network

import android.util.Log
import com.example.climasync.core.RetrofitInstance
import com.example.climasync.core.WeatherAPI
import com.example.climasync.model.WeatherResponse

class WeatherRepository {
    private val retrofit = RetrofitInstance.getRetrofit().create(WeatherAPI::class.java)

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse? {
        return try {
            val response = retrofit.getCurrentWeather(
                apiKey ="ad8d7511f63c4e00baa173252252804", // Reemplazar con tu API key real
                query = "$lat,$lon"
            )
            Log.d("WEATHER_API", "Datos obtenidos: ${response.body()}")
            response.body()
        } catch (e: Exception) {
            Log.e("WEATHER_ERROR", "Error en la solicitud: ${e.message}")
            null
        }
    }
}