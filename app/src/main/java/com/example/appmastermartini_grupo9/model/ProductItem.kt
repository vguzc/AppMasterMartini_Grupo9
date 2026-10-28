package com.example.appmastermartini_grupo9.model

/**
 * Modelo de datos representativo de las líneas de productos de Master Martini Chile.
 */
data class ProductItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val tag: String
)
