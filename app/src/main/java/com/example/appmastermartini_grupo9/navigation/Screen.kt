package com.example.appmastermartini_grupo9.navigation

/**
 * Sealed class para definir rutas tipo-safe en la navegación de Jetpack Compose.
 * Representa todos los módulos desarrollados en las Guías 8 a 13 para Master Martini Chile.
 */
sealed class Screen(val route: String) {
    // Guías 8, 9, 10
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object Settings : Screen("settings_page")

    // Guía 11: Formulario y Resumen
    data object Registro : Screen("registro")
    data object Resumen : Screen("resumen")

    // Guía 12: Animaciones y Persistencia con DataStore
    data object Animaciones : Screen("animaciones_page")

    // Guía 13: Recurso Nativo Cámara y Galería
    data object CamaraFoto : Screen("camara_page")

    // Ruta con argumentos para ver el detalle de un producto o categoría
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}") {
        fun buildRoute(): String {
            return "detail_page/$itemId"
        }
    }
}
