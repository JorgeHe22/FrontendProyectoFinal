package com.example.proyectofinal.Model

data class EquipoRequest(
    val id: String? = null,
    val marca: String,
    val modelo: String,
    val serial: String,
    val foto: String
)