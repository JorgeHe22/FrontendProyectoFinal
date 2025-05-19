package com.example.proyectofinal.Model

data class RegistroMovimientoResponse(
    val nombre: String,
    val documento: String,
    val equipo: String,
    val carrera: String,
    val fechaHora: String,
    val tipoMovimiento: String
)