package com.example.appmastermartini_grupo9.navigation

import android.net.Uri

/**
 * Sealed class para definir rutas tipo-safe en la navegación de Jetpack Compose.
 */
sealed class Screen(val route: String) {
    // Pantalla de Inicio / Splash Screen Animada
    data object Splash : Screen("splash_page")

    // Inicio y secciones principales
    data object Home : Screen("home_page")                // Descubre
    data object Recetas : Screen("recetas_page")
    data object Profile : Screen("profile_page")          // Productos ("Productos")
    data object CrearReceta : Screen("crear_receta_page")
    data object Animaciones : Screen("animaciones_page")  // Cursos ("Aprende")

    // Rutas del foro heredado, fuera del grafo de navegación.
    data object Comunidad : Screen("comunidad_page")

    data class PostDetalle(val postId: Int) : Screen("post_detalle/$postId")

    // Perfil del usuario
    data object CamaraFoto : Screen("camara_page")

    // Perfil público de otro creador de la comunidad (ej. Don Saco Roto)
    data class PerfilCreador(val autorNombre: String) : Screen("perfil_creador/${Uri.encode(autorNombre)}") {
        companion object {
            const val ROUTE_PATTERN = "perfil_creador/{autorNombre}"
        }
    }

    // Detalles contextuales
    data class RecetaDetalle(val recetaId: Int) : Screen("receta_detalle/$recetaId") {
        companion object {
            const val ROUTE_PATTERN = "receta_detalle/{recetaId}"
        }
    }

    // Rutas de autenticación y complementarias
    data object FontOnboarding : Screen("font_onboarding_page")
    data object Login : Screen("login_page")
    data object Registro : Screen("registro")
    data object Resumen : Screen("resumen")
    data object Settings : Screen("settings_page")

    data class Detail(val itemId: String) : Screen("detail_page/$itemId") {
        companion object {
            const val ROUTE_PATTERN = "detail_page/{itemId}"
        }
    }
}
