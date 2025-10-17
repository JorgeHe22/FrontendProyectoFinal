package com.example.proyectofinal.Model

data class HistorialRequest(
    val tipoMovimiento: String,
    val observacion: String?,
    val usuario: UsuarioMini,
    val equipo: EquipoMini
)

data class HistorialResponse(
    val id: String,
    val tipoMovimiento: String,
    val fechaHora: String?,
    val observacion: String?,
    val usuario: UsuarioMini,
    val equipo: EquipoMini
)

data class UsuarioMini(
    val id: String,
    val nombre: String? = null,
    val correo: String? = null
)

data class EquipoMini(
    val id: String,
    val marca: String? = null,
    val modelo: String? = null
)