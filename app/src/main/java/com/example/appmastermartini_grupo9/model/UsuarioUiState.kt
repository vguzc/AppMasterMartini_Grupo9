package com.example.appmastermartini_grupo9.model

/**
 * Modelo de datos que almacena posibles errores individuales de validación del formulario (Guía 11).
 */
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null
)

/**
 * Modelo principal que representa el estado del formulario de registro de usuario (Guía 11).
 */
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)
