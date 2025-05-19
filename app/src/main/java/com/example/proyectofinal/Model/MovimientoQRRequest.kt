package com.example.proyectofinal.Model


data class MovimientoQRRequest(
    val usuarioId: String,
    val equipoId: String,
    val tipoMovimiento: String
)