package com.example.proyectofinal.Model

import com.google.gson.annotations.SerializedName

data class DispositivoRequest(
    val marca: String,
    val modelo: String,
    val serial: String,
    val fotoUrl: String,
    @SerializedName("usuario_id") val usuarioId: String
)
