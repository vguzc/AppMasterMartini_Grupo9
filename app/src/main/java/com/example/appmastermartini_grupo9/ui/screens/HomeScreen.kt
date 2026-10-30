package com.example.appmastermartini_grupo9.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.model.RecetaOficial
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.FloatingBottomBar
import com.example.appmastermartini_grupo9.ui.components.MartiniTopHeader
import com.example.appmastermartini_grupo9.ui.components.PinterestCategoryTabs
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.ui.theme.MartiniCharcoal
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniKhaki
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel

/**
 * =========================================================================
 * A · DESCUBRE — TODAS LAS RECETAS EN UN ÚNICO MOSAICO VISUAL
 * - Conviven las recetas Oficiales de Master Martini (con sello verificado)
 *   y las recetas subidas por los usuarios de la comunidad.
 * - Filtro rápido "Oficiales" junto a "Todos", "Pastelería", "Chocolatería",
 *   "Bollería" y "Gelato".
 * =========================================================================
 */
@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    viewModel: MainViewModel = viewModel(),
    avatarUri: Uri? = null
) {
    val recetas by viewModel.recetasOficiales.collectAsState()
    val guardadasIds by viewModel.recetasGuardadasIds.collectAsState()

    var mostrarBusqueda by rememberSaveable { mutableStateOf(false) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var categoriaSeleccionada by rememberSaveable { mutableStateOf("Todos") }

    val categorias = listOf("Todos", "Oficiales", "Pastelería", "Chocolatería", "Bollería", "Gelato", "Salados")
    val scrollState = rememberScrollState()

    val recetasFiltradas = recetas.filter { receta ->
        val coincideCat = when (categoriaSeleccionada) {
            "Todos" -> true
            "Oficiales" -> receta.esOficialMartini
            else -> receta.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        }
        val coincideTexto = busqueda.isBlank() ||
            receta.titulo.contains(busqueda, ignoreCase = true) ||
            receta.subtitulo.contains(busqueda, ignoreCase = true) ||
            receta.autor.contains(busqueda, ignoreCase = true) ||
            receta.ingredientes.any { it.contains(busqueda, ignoreCase = true) }
        coincideCat && coincideTexto
    }

    val columnaIzquierda = recetasFiltradas.filterIndexed { index, _ -> index % 2 == 0 }
    val columnaDerecha = recetasFiltradas.filterIndexed { index, _ -> index % 2 == 1 }

    val alturasIzquierda = listOf(262.dp, 296.dp, 244.dp, 276.dp)
    val alturasDerecha = listOf(298.dp, 232.dp, 272.dp, 250.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(
                    PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 110.dp
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Cabecera limpia: "Descubre" + "+" (Subir receta) + Avatar unificado
            MartiniTopHeader(
                title = "Descubre",
                onCreateClick = { viewModel.navigateTo(Screen.CrearReceta) },
                avatarUri = avatarUri,
                onAvatarClick = { viewModel.navigateTo(Screen.CamaraFoto) },
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // 2. Barra "Todos · Oficiales · Pastelería..." con subrayado inferior
            PinterestCategoryTabs(
                tabs = categorias,
                selectedTab = categoriaSeleccionada,
                onSelectTab = { categoriaSeleccionada = it },
                searchQuery = busqueda,
                onSearchQueryChange = { busqueda = it },
                searchPlaceholder = "Encontrar recetas, ingredientes o autores...",
                searchVisible = mostrarBusqueda,
                onToggleSearch = {
                    if (mostrarBusqueda) busqueda = ""
                    mostrarBusqueda = !mostrarBusqueda
                },
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // 3. Mosaico escalonado de 2 columnas con Oficiales verificadas y Recetas de usuarios
            if (recetasFiltradas.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Sin recetas para mostrar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Prueba otra categoría o término de búsqueda.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Columna Izquierda
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        columnaIzquierda.forEachIndexed { idx, receta ->
                            PinterestRecipePin(
                                receta = receta,
                                cardHeight = alturasIzquierda[idx % alturasIzquierda.size],
                                esGuardada = guardadasIds.contains(receta.id),
                                onToggleGuardar = { viewModel.toggleGuardarReceta(receta.id) },
                                onClick = { viewModel.navigateTo(Screen.RecetaDetalle(receta.id)) }
                            )
                        }
                    }

                    // Columna Derecha
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        columnaDerecha.forEachIndexed { idx, receta ->
                            PinterestRecipePin(
                                receta = receta,
                                cardHeight = alturasDerecha[idx % alturasDerecha.size],
                                esGuardada = guardadasIds.contains(receta.id),
                                onToggleGuardar = { viewModel.toggleGuardarReceta(receta.id) },
                                onClick = { viewModel.navigateTo(Screen.RecetaDetalle(receta.id)) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        FloatingBottomBar(
            selectedRoute = Screen.Home.route,
            onNavigate = { screen -> viewModel.navigateBottomTab(screen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PinterestRecipePin(
    receta: RecetaOficial,
    cardHeight: Dp,
    esGuardada: Boolean,
    onToggleGuardar: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (receta.imagenUri != null) {
                AsyncImage(
                    model = receta.imagenUri,
                    contentDescription = receta.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = receta.imagenRes),
                    contentDescription = receta.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Degradado inferior ligero para asegurar lectura nítida del título
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.50f to Color.Transparent,
                                1.0f to Color(0xFF141817).copy(alpha = 0.82f)
                            )
                        )
                    )
            )

            // Esquina superior: Sello verificado en Oficiales Martini o autor en recetas de usuarios + Guardar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (receta.esOficialMartini) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MartiniGreen.copy(alpha = 0.92f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Receta oficial Master Martini",
                                tint = MartiniKhaki,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Martini",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    letterSpacing = 0.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.White
                            )
                        }
                    }
                } else {
                    val esDonSaco = receta.autor.contains("Don Saco Roto", ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.Black.copy(alpha = 0.65f)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                start = if (esDonSaco || receta.esPropiaUsuario) 3.dp else 8.dp,
                                end = 9.dp,
                                top = 3.dp,
                                bottom = 3.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (esDonSaco) {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar_don_saco_roto),
                                    contentDescription = "Don Saco Roto",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                )
                            } else if (receta.esPropiaUsuario) {
                                Surface(
                                    shape = CircleShape,
                                    color = MartiniGreen,
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = (receta.autor.ifBlank { "T" }).take(1).uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            Text(
                                text = receta.autor,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.5.sp,
                                    letterSpacing = 0.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }

                Surface(
                    onClick = onToggleGuardar,
                    shape = CircleShape,
                    color = if (esGuardada) MartiniGreen else Color.White.copy(alpha = 0.90f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (esGuardada) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (esGuardada) "Quitar de Guardados" else "Guardar receta",
                            tint = if (esGuardada) Color.White else MartiniCharcoal,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Parte inferior limpia: Solo Título de la receta (sin tiempo)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = receta.titulo,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppMasterMartini_Grupo9Theme {
        HomeScreen()
    }
}
