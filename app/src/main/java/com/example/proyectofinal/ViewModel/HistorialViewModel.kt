package com.example.proyectofinal.ViewModel

import android.util.Log
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
                Log.d("HistorialDebug", "URL: ${response.raw().request.url}  CODE: ${response.code()}")

                if (response.isSuccessful) {
                    val data = response.body().orEmpty()
                    Log.d("HistorialDebug", "Items: ${data.size}")
                    _historial.value = data
                } else {
                    Log.w("HistorialDebug", "HTTP ${response.code()}: ${response.errorBody()?.string()}")
                    _historial.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("HistorialDebug", "EX: ${e.message}", e)
                _historial.value = emptyList()
            }
        }
    }
}