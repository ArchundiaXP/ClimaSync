package com.example.climasync.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climasync.model.WeatherResponse
import com.example.climasync.network.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _mensajeweather = MutableLiveData<WeatherResponse>()
    val mensajeweather: LiveData<WeatherResponse> get() = _mensajeweather

    fun requestAPIInformation(coordinates: String) {
        _loaderState.value = true
        viewModelScope.launch {
            val response = WeatherRepository().getCurrentWeather(coordinates)
            _loaderState.value = false
            response?.let {
                _mensajeweather.value = it
            } ?: run {
                Log.e("API_ERROR", "NO SE PUDO COMPLETAR LA PETICION")
            }
        }
    }

    fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        val coordinates = "$lat,$lon"
        requestAPIInformation(coordinates)
    }
}
