package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import com.example.appmastermartini_grupo9.viewmodel.UsuarioViewModel

/**
 * =========================================================================
 * REGISTRO SCREEN (PANTALLA DE REGISTRO / SIGN UP)
 * Redirige a la nueva pantalla unificada de Login y Registro con estilo Master Martini.
 * =========================================================================
 */
@Composable
fun RegistroScreen(
    navController: NavController = rememberNavController(),
    viewModel: UsuarioViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {
    LoginRegistroScreen(
        navController = navController,
        mainViewModel = mainViewModel,
        usuarioViewModel = viewModel,
        modoInicial = ModoAuth.REGISTRO
    )
}
