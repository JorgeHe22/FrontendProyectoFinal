package com.example.proyectofinal.Model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class DispositivoRequest(
    val id: UUID? = null,
    val marca: String,
    val modelo: String,
    val serial: String,
    val fotoUrl: String,
    @SerializedName("usuario_id") val usuarioId: String
)
