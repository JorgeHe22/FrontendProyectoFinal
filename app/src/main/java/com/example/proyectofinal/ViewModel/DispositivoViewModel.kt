package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Model.EquipoRequest
import com.example.proyectofinal.Network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DispositivoViewModel(private val api: ApiService) : ViewModel() {

    private val _estadoActualizacion = MutableStateFlow<String?>(null)
    val estadoActualizacion: StateFlow<String?> = _estadoActualizacion
    val _mensajeActualizacion = MutableStateFlow<String>("")
    val mensajeActualizacion: StateFlow<String> = _mensajeActualizacion

    fun actualizarEquipo(id: String, equipo: EquipoRequest) {
        viewModelScope.launch {
            try {
                val response = api.actualizarEquipo(id, equipo)
                if (response.isSuccessful) {
                    _mensajeActualizacion.value = "✅ Equipo actualizado correctamente"
                } else {
                    _mensajeActualizacion.value = "❌ Error al actualizar equipo"
                }
            } catch (e: Exception) {
                _mensajeActualizacion.value = "⚠️ Error: ${e.localizedMessage}"
            }
        }
    }
}