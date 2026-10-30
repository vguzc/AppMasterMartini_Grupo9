package com.example.appmastermartini_grupo9.model

/**
 * Modelo de datos que almacena posibles errores individuales de validación del formulario.
 */
data class UsuarioErrores(
    val nombre: String? = null,
    val handle: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmarClave: String? = null,
    val direccion: String? = null,
    val telefono: String? = null
)

/**
 * Modelo principal que representa el estado del formulario de registro de usuario.
 */
data class UsuarioUiState(
    val nombre: String = "",
    val handle: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val aceptaTerminos: Boolean = true,
    val errores: UsuarioErrores = UsuarioErrores()
)
