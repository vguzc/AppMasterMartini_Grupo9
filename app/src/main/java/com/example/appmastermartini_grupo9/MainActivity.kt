package com.example.appmastermartini_grupo9

import android.app.KeyguardManager
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appmastermartini_grupo9.navigation.NavigationEvent
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.screens.CrearRecetaScreen
import com.example.appmastermartini_grupo9.ui.screens.FontSizeOnboardingScreen
import com.example.appmastermartini_grupo9.ui.screens.HomeScreen
import com.example.appmastermartini_grupo9.ui.screens.LoginRegistroScreen
import com.example.appmastermartini_grupo9.ui.screens.ModoAuth
import com.example.appmastermartini_grupo9.ui.screens.PantallaAnimaciones
import com.example.appmastermartini_grupo9.ui.screens.PerfilCreadorScreen
import com.example.appmastermartini_grupo9.ui.screens.PerfilFotoScreen
import com.example.appmastermartini_grupo9.ui.screens.ProfileScreen
import com.example.appmastermartini_grupo9.ui.screens.RecetaDetalleScreen
import com.example.appmastermartini_grupo9.ui.screens.RecetasScreen
import com.example.appmastermartini_grupo9.ui.screens.RegistroScreen
import com.example.appmastermartini_grupo9.ui.screens.ResumenScreen
import com.example.appmastermartini_grupo9.ui.screens.SettingsScreen
import com.example.appmastermartini_grupo9.ui.screens.SplashScreen
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.viewmodel.EstadoViewModel
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import com.example.appmastermartini_grupo9.viewmodel.PerfilViewModel
import com.example.appmastermartini_grupo9.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Actividad Principal Master Martini Chile — Arquitectura UX/UI Unificada.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        getSystemService(KeyguardManager::class.java)?.requestDismissKeyguard(this, null)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val usuarioViewModel: UsuarioViewModel = viewModel()
            val estadoViewModel: EstadoViewModel = viewModel()
            val perfilViewModel: PerfilViewModel = viewModel()

            val modoOscuroDataStore by estadoViewModel.activo.collectAsState()
            val sesionIniciadaDataStore by estadoViewModel.sesionIniciada.collectAsState()
            val usarModoOscuro = modoOscuroDataStore ?: isSystemInDarkTheme()

            // Estados de accesibilidad para adultos mayores (Tamaño de fuente global)
            val escalaFuente by estadoViewModel.escalaFuente.collectAsState()
            val onboardingFuenteMostrado by estadoViewModel.onboardingFuenteMostrado.collectAsState()

            // Si hay sesión persistida previamente en DataStore, sincronizamos los datos del usuario en MainViewModel
            LaunchedEffect(sesionIniciadaDataStore) {
                if (sesionIniciadaDataStore == true) {
                    estadoViewModel.usuarioGuardado.collectLatest { (nombre, correo, handle) ->
                        if (nombre.isNotBlank() && correo.isNotBlank()) {
                            mainViewModel.iniciarSesionConDatos(nombre, correo, handle)
                        }
                    }
                }
            }

            AppMasterMartini_Grupo9Theme(darkTheme = usarModoOscuro) {
                val densityActual = LocalDensity.current
                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = densityActual.density,
                        fontScale = densityActual.fontScale * escalaFuente
                    )
                ) {
                    val navController = rememberNavController()

                LaunchedEffect(key1 = Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                if (event.route == Screen.Home) {
                                    val popped = navController.popBackStack(Screen.Home.route, inclusive = false)
                                    if (!popped) {
                                        navController.navigate(Screen.Home.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                } else {
                                    navController.navigate(route = event.route.route) {
                                        event.popUpToRoute?.let { popup ->
                                            popUpTo(route = popup.route) {
                                                inclusive = event.inclusive
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = event.singleTop
                                        restoreState = event.popUpToRoute != null
                                    }
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

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        enterTransition = { fadeIn(animationSpec = tween(200)) },
                        exitTransition = { fadeOut(animationSpec = tween(180)) }
                    ) {
                        // 0. Splash Screen minimalista
                        composable(route = Screen.Splash.route) {
                            SplashScreen(
                                onSplashFinished = {
                                    // Flujo de inicio accesible:
                                    // 1. Si no ha configurado tamaño de letra -> Pantalla de Accesibilidad
                                    // 2. Si ya lo configuró y hay sesión -> Home
                                    // 3. De lo contrario -> Login
                                    val destino = when {
                                        onboardingFuenteMostrado != true -> Screen.FontOnboarding.route
                                        sesionIniciadaDataStore == true -> Screen.Home.route
                                        else -> Screen.Login.route
                                    }
                                    navController.navigate(destino) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // 0.B Onboarding de Accesibilidad para Adultos Mayores
                        composable(route = Screen.FontOnboarding.route) {
                            FontSizeOnboardingScreen(
                                escalaActual = escalaFuente,
                                onConfirmarEscala = { escalaElegida ->
                                    estadoViewModel.guardarEscalaFuente(escalaElegida)
                                    estadoViewModel.marcarOnboardingFuenteMostrado()
                                    val destino = if (sesionIniciadaDataStore == true) {
                                        Screen.Home.route
                                    } else {
                                        Screen.Login.route
                                    }
                                    navController.navigate(destino) {
                                        popUpTo(Screen.FontOnboarding.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                onOmitir = {
                                    estadoViewModel.marcarOnboardingFuenteMostrado()
                                    val destino = if (sesionIniciadaDataStore == true) {
                                        Screen.Home.route
                                    } else {
                                        Screen.Login.route
                                    }
                                    navController.navigate(destino) {
                                        popUpTo(Screen.FontOnboarding.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        // Inicio y catálogo de recetas
                        composable(route = Screen.Home.route) {
                            HomeScreen(viewModel = mainViewModel)
                        }

                        composable(route = Screen.Recetas.route) {
                            RecetasScreen(
                                viewModel = mainViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // 2. B · PRODUCTOS ("Productos")
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(
                                navController = navController,
                                viewModel = mainViewModel
                            )
                        }

                        // 3. ACCIÓN CENTRAL "+" ("Crear receta")
                        composable(route = Screen.CrearReceta.route) {
                            CrearRecetaScreen(
                                viewModel = mainViewModel,
                                onClose = { navController.popBackStack() }
                            )
                        }

                        // 4. C · CURSOS ("Aprende")
                        composable(route = Screen.Animaciones.route) {
                            PantallaAnimaciones(mainViewModel = mainViewModel)
                        }

                        // Perfil del usuario
                        composable(route = Screen.CamaraFoto.route) {
                            PerfilFotoScreen(
                                viewModel = perfilViewModel,
                                mainViewModel = mainViewModel,
                                estadoViewModel = estadoViewModel
                            )
                        }

                        // 7. DETALLE DE RECETA OFICIAL
                        composable(
                            route = Screen.RecetaDetalle.ROUTE_PATTERN,
                            arguments = listOf(navArgument("recetaId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val recetaId = backStackEntry.arguments?.getInt("recetaId") ?: 100
                            RecetaDetalleScreen(
                                recetaId = recetaId,
                                viewModel = mainViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }


                        // 9. PERFIL PÚBLICO DE CREADOR (DON SACO ROTO)
                        composable(
                            route = Screen.PerfilCreador.ROUTE_PATTERN,
                            arguments = listOf(navArgument("autorNombre") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val autorNombre = backStackEntry.arguments?.getString("autorNombre") ?: "Don Saco Roto"
                            PerfilCreadorScreen(
                                autorNombre = autorNombre,
                                mainViewModel = mainViewModel,
                                onBack = {
                                    val popped = navController.popBackStack()
                                    if (!popped) {
                                        mainViewModel.navigateBottomTab(Screen.Home)
                                    }
                                }
                            )
                        }

                        // 10. Autenticación y rutas complementarias
                        composable(route = Screen.Login.route) {
                            LoginRegistroScreen(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                usuarioViewModel = usuarioViewModel,
                                perfilViewModel = perfilViewModel,
                                estadoViewModel = estadoViewModel,
                                modoInicial = ModoAuth.LOGIN
                            )
                        }

                        composable(route = Screen.Registro.route) {
                            LoginRegistroScreen(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                usuarioViewModel = usuarioViewModel,
                                perfilViewModel = perfilViewModel,
                                estadoViewModel = estadoViewModel,
                                modoInicial = ModoAuth.REGISTRO
                            )
                        }

                        composable(route = Screen.Settings.route) {
                            SettingsScreen(
                                navController = navController,
                                viewModel = mainViewModel,
                                estadoViewModel = estadoViewModel
                            )
                        }

                        composable(route = Screen.Resumen.route) {
                            ResumenScreen(navController = navController, viewModel = usuarioViewModel)
                        }
                    }
                }
            }
        }
    }
}
}
