package com.example.proyectofinal.ViewModel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Model.DispositivoRequest
import com.example.proyectofinal.Model.LoginRequest
import com.example.proyectofinal.Model.UsuarioRequest
import com.example.proyectofinal.Model.UsuarioResponse
import com.example.proyectofinal.Network.RetrofitClient
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {
    var usuarioLogueado by mutableStateOf<UsuarioResponse?>(null)
        private set

    fun registrarUsuario(usuario: UsuarioRequest, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.registrarUsuario(usuario)
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                Log.e("RegistroUsuario", "Error: ${e.message}")
                onResult(false)
            }
        }
    }

    fun loginEstudiante(documento: String, correo: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(documento = documento, correo = correo)
                val response = RetrofitClient.apiService.loginEstudiante(request)

                if (response.isSuccessful) {
                    val usuario = response.body()
                    Log.d("LoginDebug", "Usuario recibido: $usuario")
                    usuarioLogueado = usuario
                    onResult(usuario != null)
                } else {
                    Log.e("LoginDebug", "Login fallido con código: ${response.code()}")
                    onResult(false)
                }

            } catch (e: Exception) {
                Log.e("LoginEstudiante", "Error: ${e.message}")
                onResult(false)
            }
        }
    }
    var dispositivoRegistrado by mutableStateOf<DispositivoRequest?>(null)
        private set

    fun obtenerDispositivo(usuarioId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.obtenerEquiposPorUsuario(usuarioId)
                if (response.isSuccessful) {
                    val lista = response.body()
                    dispositivoRegistrado = lista?.firstOrNull()  // ← toma solo el primero
                    Log.d("Equipo", "Dispositivo cargado: ${dispositivoRegistrado?.marca}")
                } else {
                    Log.e("Equipo", "Error al obtener dispositivo: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Equipo", "Excepción: ${e.message}")
            }
        }
    }

    fun registrarDispositivo(
        request: DispositivoRequest,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.registrarDispositivo(request)
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
    fun registrarMovimientoDesdeQR(
        datosEscaneados: String,
        tipoMovimiento: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.registrarMovimientoDesdeQR(
                    datosEscaneados = datosEscaneados,
                    tipoMovimiento = tipoMovimiento
                )

                if (response.isSuccessful) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
    fun loginGuardia(documento: String, correo: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(documento, correo)
                val response = RetrofitClient.apiService.loginEstudiante(request)

                if (response.isSuccessful) {
                    val usuario = response.body()
                    Log.d("LoginGuardia", "Usuario recibido: $usuario")

                    if (usuario != null && usuario.rol.equals("GUARDIA", ignoreCase = true)) {
                        usuarioLogueado = usuario
                        onResult(true)
                    } else {
                        Log.d("LoginGuardia", "Usuario no es guardia o es nulo")
                        onResult(false)
                    }
                } else {
                    Log.e("LoginGuardia", "Respuesta no exitosa: ${response.code()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("LoginGuardia", "Error: ${e.message}")
                onResult(false)
            }
        }
    }
}