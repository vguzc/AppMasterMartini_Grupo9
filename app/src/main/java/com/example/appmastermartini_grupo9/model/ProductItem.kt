package com.example.appmastermartini_grupo9.model

import androidx.annotation.DrawableRes

/**
 * Modelo de datos representativo de las líneas de productos de Master Martini Chile.
 */
data class ProductItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val tag: String,
    val formato: String = "Formato Profesional Horeca",
    val aplicacion: String = "Pastelería y Chocolatería",
    @param:DrawableRes val imagenRes: Int = 0
)
