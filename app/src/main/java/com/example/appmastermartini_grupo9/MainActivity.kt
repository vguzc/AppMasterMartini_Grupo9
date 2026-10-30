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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appmastermartini_grupo9.navigation.NavigationEvent
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.screens.ComunidadScreen
import com.example.appmastermartini_grupo9.ui.screens.CrearRecetaScreen
import com.example.appmastermartini_grupo9.ui.screens.HomeScreen
import com.example.appmastermartini_grupo9.ui.screens.LoginRegistroScreen
import com.example.appmastermartini_grupo9.ui.screens.ModoAuth
import com.example.appmastermartini_grupo9.ui.screens.PantallaAnimaciones
import com.example.appmastermartini_grupo9.ui.screens.PerfilCreadorScreen
import com.example.appmastermartini_grupo9.ui.screens.PerfilFotoScreen
import com.example.appmastermartini_grupo9.ui.screens.PostDetalleScreen
import com.example.appmastermartini_grupo9.ui.screens.ProfileScreen
import com.example.appmastermartini_grupo9.ui.screens.RecetaDetalleScreen
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
 * Navegación inferior de 5 elementos: Recetas · Productos · + Crear · Cursos · Comunidad.
 * Perfil ("Mi espacio") y Guardados accesibles desde el Avatar superior derecho.
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
            val avatarUri by perfilViewModel.imagenUri.collectAsState()
            val usarModoOscuro = modoOscuroDataStore ?: isSystemInDarkTheme()

            AppMasterMartini_Grupo9Theme(darkTheme = usarModoOscuro) {
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
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // 1. A · RECETAS ("Descubre")
                        composable(route = Screen.Home.route) {
                            HomeScreen(
                                navController = navController,
                                viewModel = mainViewModel,
                                avatarUri = avatarUri
                            )
                        }

                        // 2. B · PRODUCTOS ("Productos")
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(
                                navController = navController,
                                viewModel = mainViewModel,
                                avatarUri = avatarUri
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
                            PantallaAnimaciones(
                                mainViewModel = mainViewModel,
                                avatarUri = avatarUri
                            )
                        }

                        // 5. D · COMUNIDAD ("Comunidad")
                        composable(route = Screen.Comunidad.route) {
                            ComunidadScreen(
                                viewModel = mainViewModel,
                                avatarUri = avatarUri
                            )
                        }

                        // 6. PERFIL ("Mi espacio" desde Avatar superior)
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

                        // 8. DETALLE DE PUBLICACIÓN E HILO DE CONVERSACIÓN EN COMUNIDAD
                        composable(
                            route = Screen.PostDetalle.ROUTE_PATTERN,
                            arguments = listOf(navArgument("postId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val postId = backStackEntry.arguments?.getInt("postId") ?: 201
                            PostDetalleScreen(
                                postId = postId,
                                viewModel = mainViewModel,
                                avatarUriUsuario = avatarUri,
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
                                modoInicial = ModoAuth.LOGIN
                            )
                        }

                        composable(route = Screen.Registro.route) {
                            LoginRegistroScreen(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                usuarioViewModel = usuarioViewModel,
                                perfilViewModel = perfilViewModel,
                                modoInicial = ModoAuth.REGISTRO
                            )
                        }

                        composable(route = Screen.Settings.route) {
                            SettingsScreen(navController = navController, viewModel = mainViewModel)
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
