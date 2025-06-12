package com.example.climasync.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climasync.network.WeatherRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class RegistroViewModel : ViewModel() {

@HiltViewModel
    class PersonalInformationViewModel @Inject constructor(
        private val repository: WeatherRepository
    ):ViewModel(){

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _validRegister = MutableLiveData<Boolean>()
    val validRegister: LiveData<Boolean> get() = _validRegister

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val firebase = FirebaseAuth.getInstance()

    fun requestSignUp(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            _loaderState.value = true

            viewModelScope.launch {
                try {
                    val result = firebase.createUserWithEmailAndPassword(email, password).await()
                    _loaderState.value = false

                    result.user?.let {
                        Log.i("Firebase", "Usuario registrado con éxito.")
                        _validRegister.value = true
                    } ?: run {
                        Log.e("Firebase", "Usuario nulo después del registro.")
                        _errorMessage.value = "Ocurrió un error inesperado. Intenta nuevamente."
                        _validRegister.value = false
                    }

                } catch (e: FirebaseAuthWeakPasswordException) {
                    _loaderState.value = false
                    Log.e("Firebase", "Contraseña débil: ${e.message}")
                    _errorMessage.value = "La contraseña debe tener al menos 6 caracteres."
                    _validRegister.value = false

                } catch (e: FirebaseAuthUserCollisionException) {
                    _loaderState.value = false
                    Log.e("Firebase", "Correo ya registrado: ${e.message}")
                    _errorMessage.value = "El correo ya está en uso. Usa otro o inicia sesión."
                    _validRegister.value = false

                } catch (e: Exception) {
                    _loaderState.value = false
                    Log.e("Firebase", "Error desconocido: ${e.message}")
                    _errorMessage.value = "Error al registrar: ${e.localizedMessage ?: "desconocido"}"
                    _validRegister.value = false
                }
            }

        } else {
            _errorMessage.value = "Debes completar todos los campos."
            _validRegister.value = false
        }
    }
}
