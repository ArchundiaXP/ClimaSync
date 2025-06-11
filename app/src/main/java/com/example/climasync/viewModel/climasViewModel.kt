package com.example.climasync.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climasync.core.ResultWrapper
import com.example.climasync.network.WeatherRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class climasViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState
    private val _climaInfo = MutableLiveData<List<Clima>>()
    val climaInfo: LiveData<List<Clima>>
        get() = _climaInfo

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getForecastWeather() {
        _loaderState.value = true
        viewModelScope.launch {
            when (val result = repository.getForecastWeather()) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _climaInfo.value = result.data
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    _errorMessage.value = result.exception.message
                }
            }
        }
    }
}