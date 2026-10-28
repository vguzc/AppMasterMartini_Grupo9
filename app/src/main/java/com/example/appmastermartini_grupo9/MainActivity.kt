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
import com.example.appmastermartini_grupo9.ui.screens.ProfileScreen
import com.example.appmastermartini_grupo9.ui.screens.SettingsScreen
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Actividad Principal (Guía 8, Guía 9 y Guía 10).
 * Integra el patrón arquitectónico MVVM desacoplando la navegación de la interfaz
 * mediante SharedFlow y Jetpack Compose Navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppMasterMartini_Grupo9Theme {
                // ViewModel y NavController
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                // Escuchar eventos de navegación emitidos por el ViewModel (Guía 10)
                LaunchedEffect(key1 = Unit) {
                    viewModel.navigationEvents.collectLatest { event ->
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
                        // Pantalla 1: Home con ModalNavigationDrawer y TopAppBar (Guías 8 y 10)
                        composable(route = Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }

                        // Pantalla 2: Catálogo de Productos / Perfil con NavigationBar (Guía 10)
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = viewModel)
                        }

                        // Pantalla 3: Información y Contacto Master Martini (Guía 10)
                        composable(route = Screen.Settings.route) {
                            SettingsScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
