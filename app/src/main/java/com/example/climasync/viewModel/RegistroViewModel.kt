package com.example.climasync.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
class RegistroViewModel : ViewModel() {

    // publisher para mostrar loader
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    // publisher para validar sesión (registro exitoso)
    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean>
        get() = _sessionValid

    // publisher para errores
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    // instancia de Firebase Auth
    private val firebase = FirebaseAuth.getInstance()

    // función de registro con corrutina
    fun requestSignUp(email: String, password: String) {
        _loaderState.value = true // activamos loader

        viewModelScope.launch {
            try {
                val result = firebase.createUserWithEmailAndPassword(email, password).await()
                _loaderState.value = false // desactivamos loader

                result.user?.let {
                    _sessionValid.value = true // registro exitoso
                } ?: run {
                    _errorMessage.value = "No se pudo crear el usuario."
                    Log.i("firebase", "Usuario null luego del registro")
                }

            } catch (e: Exception) {
                _loaderState.value = false
                _errorMessage.value = e.message ?: "Error desconocido"
                Log.e("firebase", "Error en el registro: ${e.message}")
            }
        }
    }
}