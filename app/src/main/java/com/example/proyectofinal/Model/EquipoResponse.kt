package com.example.proyectofinal.Model

data class EquipoResponse(
    val id: String,
    val marca: String,
    val modelo: String,
    val serial: String,
    val foto: String?,
    val usuarioId: String
)