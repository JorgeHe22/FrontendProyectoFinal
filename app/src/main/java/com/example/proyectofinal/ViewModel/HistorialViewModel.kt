package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Model.HistorialResponse
import com.example.proyectofinal.Network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistorialViewModel(private val api: ApiService) : ViewModel() {

    private val _historial = MutableStateFlow<List<HistorialResponse>>(emptyList())
    val historial: StateFlow<List<HistorialResponse>> = _historial

    fun cargarHistorial(usuarioId: String) {
        viewModelScope.launch {
            try {
                val response = api.getHistorial(usuarioId)
                if (response.isSuccessful) {
                    _historial.value = response.body() ?: emptyList()
                } else {
                    _historial.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _historial.value = emptyList()
            }
        }
    }
}