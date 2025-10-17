package com.example.proyectofinal.Model

data class HistorialRequest(
    val tipoMovimiento: String,
    val observacion: String?,
    val usuario: UsuarioHistorial,
    val equipo: EquipoHistorialResponse
)

data class HistorialResponse(
    val tipoMovimiento: String,
    val fechaHora: String?,
    val equipo: EquipoHistorialResponse
)

data class UsuarioHistorial(
    val id: String,
    val nombre: String,
    val documento: String,
    val correo: String,
    val carrera: String,
    val rol: String
)

data class EquipoHistorialResponse(
    val marca: String?,
    val modelo: String?,
    val numeroSerie: String?
)