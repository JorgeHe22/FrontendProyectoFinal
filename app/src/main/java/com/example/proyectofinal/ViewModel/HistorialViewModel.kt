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
                    val data = response.body() ?: emptyList()
                    _historial.value = data
                    println("✅ Historial recibido (${data.size} items): $data")
                } else {
                    println("⚠️ Error en respuesta HTTP: ${response.code()}")
                    _historial.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("❌ Excepción en cargarHistorial: ${e.message}")
                _historial.value = emptyList()
            }
        }
    }
}