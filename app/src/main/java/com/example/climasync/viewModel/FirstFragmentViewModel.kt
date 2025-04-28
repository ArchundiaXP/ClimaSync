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

class FirstFragmentViewModel: ViewModel() {
    //publisher para mostrar loader
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    //publisher para validar sesion
    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean>
        get() = _sessionValid

    //traemos la intancia de la autentificacion firebase
    private val firebase = FirebaseAuth.getInstance()

    //corrutina salto de loader con validador con firebase
    fun requestSingnIn(email: String, password: String){
        _loaderState.value = true //activamos loader

        viewModelScope.launch {//lanzamos la corrutina
            val result = firebase.signInWithEmailAndPassword(email, password).await()
            _loaderState.value = false //desactivamos loader
            result.user?.let { //si el usuario existe
                delay(5000)
                _sessionValid.value = true //activamos validador
            } ?: run { //si no existe
                //mandamos mensaje
                Log.i("firebase", "Ocurrio un error querido contribuidor :0")
            }
        }
    }
}