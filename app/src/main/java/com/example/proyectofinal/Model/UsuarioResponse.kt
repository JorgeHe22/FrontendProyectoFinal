package com.example.proyectofinal.Model

data class UsuarioResponse(
    val id: String,
    val nombre: String,
    val documento: String,
    val correo: String,
    val carrera: String,
    val rol: String
)