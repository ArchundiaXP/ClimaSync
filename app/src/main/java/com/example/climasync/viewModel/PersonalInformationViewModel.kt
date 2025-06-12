package com.example.climasync.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climasync.network.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class PersonalInformationViewModel: ViewModel()  {
    //publisher para mostrar loader

    @HiltViewModel
    class PersonalInformationViewModel @Inject constructor(
        private val repository: WeatherRepository
    ):ViewModel(){

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    //publisher para mensaje
    private val _msj = MutableLiveData<Boolean>()
    val msj: LiveData<Boolean>
        get() = _msj

    fun requestPersonalInformation(nombre: String, apellidos: String, nombreUsuario: String, fechaNacimiento: String, codigoReferido: String){
        _loaderState.value = true //activamos loader

        viewModelScope.launch {
            delay(5000)
            _loaderState.value = false
            _msj.value = true //activamos mensaje
        }


    }
}