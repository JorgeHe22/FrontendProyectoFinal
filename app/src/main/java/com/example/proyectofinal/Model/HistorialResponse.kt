package com.example.proyectofinal.Model

data class HistorialRequest(
    val tipoMovimiento: String,
    val observacion: String?,
    val usuario: UsuarioHistorial,
    val equipo: EquipoHistorial
)

data class HistorialResponse(
    val id: String,
    val tipoMovimiento: String,
    val fechaHora: String?,
    val observacion: String?,
    val usuario: UsuarioHistorial,
    val equipo: EquipoHistorial
)

data class UsuarioHistorial(
    val id: String,
    val nombre: String,
    val documento: String,
    val correo: String,
    val carrera: String,
    val rol: String
)

data class EquipoHistorial(
    val id: String,
    val marca: String,
    val modelo: String,
    val serial: String
)