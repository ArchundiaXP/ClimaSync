package com.example.climasync.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climasync.network.WeatherRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class LoginViewModel : ViewModel() {
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: WeatherRepository
):ViewModel(){

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean> get() = _sessionValid

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val firebase = FirebaseAuth.getInstance()

    fun requestSingnIn(email: String, password: String) {
        _loaderState.value = true

        viewModelScope.launch {
            try {
                val result = firebase.signInWithEmailAndPassword(email, password).await()
                _loaderState.value = false
                result.user?.let {
                    Log.i("firebase", "Inicio de sesión exitoso.")
                    _sessionValid.value = true
                } ?: run {
                    Log.e("firebase", "Usuario nulo.")
                    _errorMessage.value = "Ocurrió un error al iniciar sesión."
                    _sessionValid.value = false
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                _loaderState.value = false
                Log.e("firebase", "Usuario no encontrado: ${e.message}")
                _errorMessage.value = "Este usuario no está registrado."
                _sessionValid.value = false
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _loaderState.value = false
                Log.e("firebase", "Credenciales inválidas: ${e.message}")
                _errorMessage.value = "Correo o contraseña incorrectos."
                _sessionValid.value = false
            } catch (e: Exception) {
                _loaderState.value = false
                Log.e("firebase", "Error inesperado: ${e.message}")
                _errorMessage.value = "Error inesperado: ${e.localizedMessage}"
                _sessionValid.value = false
            }
        }
    }
}
