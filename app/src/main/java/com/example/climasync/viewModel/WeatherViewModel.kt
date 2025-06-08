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
    //publisher para mostrar loader
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    //mensaje
    private val _mensajeweather = MutableLiveData<WeatherResponse>()
    val mensajeweather: LiveData<WeatherResponse>
        get() = _mensajeweather

    fun requestAPIInformation(coordinates: String) {
        _loaderState.value = true //activamos loader
        viewModelScope.launch {
            val response = WeatherRepository().getCurrentWeather(coordinates)
            _loaderState.value = false //desactivamos loader
            response?.let {
                _mensajeweather.value = it
            } ?: run {
                Log.e("API_ERROR", "NO SE PUDO COMPLETAR LA PETICION")
            }
        }

    }
}