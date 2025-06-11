package com.example.climasync.model

import com.google.gson.annotations.SerializedName


data class WeatherResponse (
    val location: Location,
    val current: Current
){

data class Location(

    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id") val tzId: String,
    @SerializedName("localtime") val localTime: String

)

data class Current(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    val condition: Condition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDir: String,
    val humidity: Int,
    val uv: Double,
    @SerializedName("feelslike_c") val feelsLikeC: Double
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)
}