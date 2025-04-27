package com.example.climasync.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistroViewModel : ViewModel() {

    //publisher para el loader
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    //publisher para el registro
    private val _validRegister = MutableLiveData<Boolean>()
    val validRegister: LiveData<Boolean>
        get() = _validRegister //obtiene boolean de register

    private val firebase = FirebaseAuth.getInstance() //instancia

    //funcion para registrar
    fun requestSignUp(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            _loaderState.value = true //muestra loader por que comienza corrutina

            viewModelScope.launch { //inicia corrutina
                val result = firebase.createUserWithEmailAndPassword(email, password).await()
                _loaderState.value = false //cuando termina corrutina oculta loader
                //mensajes para verificar estado de registro
                result.user?.let {
                    delay(5000)
                    Log.i("Firebase", "Se creó al usuario con éxito.")
                    _validRegister.value = true
                } ?: run {
                    delay(5000)
                    Log.e("Firebase", "Ocurrio un problema al crear al usuario.")

                }
            }
        } else {
            _validRegister.value = false
        }
    }

}