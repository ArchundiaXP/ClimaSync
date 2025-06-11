package com.example.climasync.viewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climasync.model.ForecastResponse
import com.example.climasync.model.WeatherResponse
import com.example.climasync.network.WeatherRepository
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _mensajeweather = MutableLiveData<WeatherResponse>()
    val mensajeweather: LiveData<WeatherResponse> get() = _mensajeweather

    private val _forecast = MutableLiveData<List<ForecastResponse.ForecastDay>>()
    val forecast: LiveData<List<ForecastResponse.ForecastDay>> get() = _forecast

    fun requestAPIInformation(coordinates: String) {
        _loaderState.value = true
        viewModelScope.launch {
            val response = repository.getCurrentWeather(coordinates)
            _loaderState.value = false
            response?.let {
                _mensajeweather.value = it
            } ?: run {
                Log.e("API_ERROR", "NO SE PUDO COMPLETAR LA PETICION")
            }
        }
    }

    fun fetchForecastInformation(coordinates: String) {
        _loaderState.value = true
        viewModelScope.launch {
            val forecastResponse = repository.getForecastWeather(coordinates)
            _loaderState.value = false
            forecastResponse?.let {
                _forecast.value = it
            } ?: run {
                Log.e("FORECAST_ERROR", "No se pudo obtener el pronóstico")
            }
        }
    }

    fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        val coordinates = "$lat,$lon"
        requestAPIInformation(coordinates)
        fetchForecastInformation(coordinates)
    }
}

