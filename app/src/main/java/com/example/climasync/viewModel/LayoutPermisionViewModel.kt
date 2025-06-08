package com.example.climasync.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LayoutPermisionViewModel : ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _msj = MutableLiveData<Boolean>()
    val msj: LiveData<Boolean>
        get() = _msj

    fun requestLayoutPermision() {
       _msj.value = true
    }
}
