package com.example.appmastermartini_grupo9.navigation

/**
 * Sealed class para definir rutas tipo-safe en la navegación de Jetpack Compose (Guía 10).
 * Representa los destinos principales de la aplicación Master Martini Chile.
 */
sealed class Screen(val route: String) {
    // Rutas simples (sin argumentos)
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object Settings : Screen("settings_page")

    // Ruta con argumentos para ver el detalle de un producto o categoría de Master Martini
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}") {
        fun buildRoute(): String {
            return "detail_page/$itemId"
        }
    }
}
