package com.example.appmastermartini_grupo9.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmastermartini_grupo9.ui.screens.RegistroScreen
import com.example.appmastermartini_grupo9.ui.screens.ResumenScreen
import com.example.appmastermartini_grupo9.viewmodel.UsuarioViewModel

/**
 * =========================================================================
 * GUÍA 11: NAVEGACIÓN ENTRE FORMULARIO DE REGISTRO Y RESUMEN
 * Demuestra el uso de un ViewModel compartido sin necesidad de pasar argumentos
 * =========================================================================
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Creamos el ViewModel una sola vez para compartir el estado entre ambas pantallas
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "registro"
    ) {
        composable(route = "registro") {
            RegistroScreen(navController = navController, viewModel = usuarioViewModel)
        }
        composable(route = "resumen") {
            ResumenScreen(navController = navController, viewModel = usuarioViewModel)
        }
    }
}
