package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Model.DispositivoRequest
import com.example.proyectofinal.Model.EquipoRequest
import com.example.proyectofinal.Network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DispositivoViewModel(
    private val api: ApiService
) : ViewModel() {

    // 🟣 Mensaje para mostrar en la UI
    private val _mensajeActualizacion = MutableStateFlow("")
    val mensajeActualizacion: StateFlow<String> = _mensajeActualizacion

    // 🟢 El equipo actualmente seleccionado para editar
    private val _equipoActual = MutableStateFlow<DispositivoRequest?>(null)
    val equipoActualFlow: StateFlow<DispositivoRequest?> = _equipoActual

    // 🧩 Guardar el equipo seleccionado (desde AccionesEstudiante)
    fun setEquipoActual(equipo: DispositivoRequest) {
        println("📦 setEquipoActual -> ${equipo.id} (${equipo.marca}/${equipo.modelo})")
        _equipoActual.value = equipo
    }

    // 🚀 Ejecutar el PUT en el backend
    fun actualizarEquipo(id: String, equipo: DispositivoRequest) {
        viewModelScope.launch {
            try {
                println("🌐 Llamando PUT /api/equipos/$id con body=${equipo}")

                val response = api.actualizarEquipo(id, equipo)

                if (response.isSuccessful) {
                    _mensajeActualizacion.value = "✅ Equipo actualizado correctamente"
                    _equipoActual.value = equipo // 🔄 refresca estado en Compose
                    println("✅ PUT OK (${response.code()})")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _mensajeActualizacion.value = "❌ Error al actualizar (${response.code()})"
                    println("❌ PUT ERROR ${response.code()} -> $errorBody")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _mensajeActualizacion.value = "❌ Error: ${e.localizedMessage}"
                println("💥 Excepción al actualizar equipo: ${e.localizedMessage}")
            }
        }
    }
}