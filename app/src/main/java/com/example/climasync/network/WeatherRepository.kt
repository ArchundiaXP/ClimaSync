package com.example.climasync.network

import android.util.Log
import com.example.climasync.core.RetrofitInstance
import com.example.climasync.core.WeatherAPI
import com.example.climasync.model.ForecastResponse
import com.example.climasync.model.WeatherResponse
import jakarta.inject.Inject


class WeatherRepository {
    private val retrofit = RetrofitInstance.getRetrofit().create(WeatherAPI::class.java)

    class WeatherRepository @Inject constructor(
        private val weatherAPI: WeatherAPI
    ){

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
    suspend fun getForecastWeather(coordinates: String): List<ForecastResponse.ForecastDay>? {
        return try {
            val response = retrofit.getForecast(
                apiKey = "2c3b876526d649368b8143309250606",
                coordinates = coordinates,
                days = 7
            )
            Log.d("FORECAST_API", "Forecast recibido: ${response.body()}")
            response.body()?.forecast?.forecastday
        } catch (e: Exception) {
            Log.e("FORECAST_ERROR", "Error en forecast: ${e.message}")
            null
        }
    }
}