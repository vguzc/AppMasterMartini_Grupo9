package com.example.appmastermartini_grupo9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmastermartini_grupo9.navigation.NavigationEvent
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.screens.HomeScreen
import com.example.appmastermartini_grupo9.ui.screens.PantallaAnimaciones
import com.example.appmastermartini_grupo9.ui.screens.PerfilFotoScreen
import com.example.appmastermartini_grupo9.ui.screens.ProfileScreen
import com.example.appmastermartini_grupo9.ui.screens.RegistroScreen
import com.example.appmastermartini_grupo9.ui.screens.ResumenScreen
import com.example.appmastermartini_grupo9.ui.screens.SettingsScreen
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import com.example.appmastermartini_grupo9.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Actividad Principal Master Martini Chile (Guías 8, 9, 10, 11, 12, 13).
 * Centraliza la navegación reactiva mediante SharedFlow y conecta todas las pantallas
 * de las actividades prácticas con ViewModel compartido y arquitectura MVVM.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppMasterMartini_Grupo9Theme {
                // ViewModels
                val mainViewModel: MainViewModel = viewModel()
                val usuarioViewModel: UsuarioViewModel = viewModel() // Compartido para Guía 11
                val navController = rememberNavController()

                // Escucha reactiva de eventos de navegación emitidos por MainViewModel (Guía 10)
                LaunchedEffect(key1 = Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(route = event.route.route) {
                                    event.popUpToRoute?.let { popup ->
                                        popUpTo(route = popup.route) {
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }
                            is NavigationEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                            is NavigationEvent.NavigateUp -> {
                                navController.navigateUp()
                            }
                        }
                    }
                }

                // Layout base con NavHost
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Pantalla Home (Guías 8 y 10)
                        composable(route = Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = mainViewModel)
                        }

                        // 2. Catálogo de Productos / Perfil (Guía 10)
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = mainViewModel)
                        }

                        // 3. Información y Contacto (Guía 10)
                        composable(route = Screen.Settings.route) {
                            SettingsScreen(navController = navController, viewModel = mainViewModel)
                        }

                        // 4. Formulario de Registro de Usuario (Guía 11)
                        composable(route = Screen.Registro.route) {
                            RegistroScreen(navController = navController, viewModel = usuarioViewModel)
                        }

                        // 5. Resumen de Datos con ViewModel Compartido (Guía 11)
                        composable(route = Screen.Resumen.route) {
                            ResumenScreen(navController = navController, viewModel = usuarioViewModel)
                        }

                        // 6. Animaciones y Persistencia con DataStore (Guía 12)
                        composable(route = Screen.Animaciones.route) {
                            PantallaAnimaciones()
                        }

                        // 7. Foto de Perfil con Cámara y Galería Nativas (Guía 13)
                        composable(route = Screen.CamaraFoto.route) {
                            PerfilFotoScreen()
                        }
                    }
                }
            }
        }
    }
}
