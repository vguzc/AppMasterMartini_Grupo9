package com.example.appmastermartini_grupo9.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmastermartini_grupo9.model.ProductItem
import com.example.appmastermartini_grupo9.model.RecetaOficial
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.FloatingBottomBar
import com.example.appmastermartini_grupo9.ui.components.MartiniTopHeader
import com.example.appmastermartini_grupo9.ui.components.PinterestCategoryTabs
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel

/**
 * =========================================================================
 * B · PRODUCTOS — CATÁLOGO PROFESIONAL CON BARRA ESTILO PINTEREST
 * =========================================================================
 */
@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController(),
    viewModel: MainViewModel = viewModel(),
    avatarUri: Uri? = null
) {
    val products = viewModel.catalogoProductos
    val recetas by viewModel.recetasOficiales.collectAsState()
    val destacadoId by viewModel.productoDestacadoId.collectAsState()

    var mostrarBusqueda by rememberSaveable { mutableStateOf(false) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var categoriaActiva by rememberSaveable { mutableStateOf("Todos") }
    var productoExpandidoId by rememberSaveable { mutableStateOf<String?>(products.firstOrNull()?.id) }

    LaunchedEffect(destacadoId) {
        if (destacadoId != null) {
            categoriaActiva = "Todos"
            productoExpandidoId = destacadoId
            viewModel.limpiarProductoDestacado()
        }
    }

    val categorias = listOf(
        "Todos",
        "Chocolatería",
        "Cremas y Coberturas",
        "Cremas Vegetales",
        "Plant Based",
        "Margarinas"
    )

    val productosFiltrados = products.filter { prod ->
        val coincideCat = categoriaActiva == "Todos" ||
            prod.category.equals(categoriaActiva, ignoreCase = true)
        val coincideBusqueda = busqueda.isBlank() ||
            prod.name.contains(busqueda, ignoreCase = true) ||
            prod.description.contains(busqueda, ignoreCase = true) ||
            prod.tag.contains(busqueda, ignoreCase = true) ||
            prod.aplicacion.contains(busqueda, ignoreCase = true)
        coincideCat && coincideBusqueda
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                MartiniTopHeader(
                    title = "Productos",
                    avatarUri = avatarUri,
                    onAvatarClick = { viewModel.navigateTo(Screen.CamaraFoto) }
                )
            }

            item {
                PinterestCategoryTabs(
                    tabs = categorias,
                    selectedTab = categoriaActiva,
                    onSelectTab = { categoriaActiva = it },
                    searchQuery = busqueda,
                    onSearchQueryChange = { busqueda = it },
                    searchPlaceholder = "Encontrar productos e insumos...",
                    searchVisible = mostrarBusqueda,
                    onToggleSearch = {
                        if (mostrarBusqueda) busqueda = ""
                        mostrarBusqueda = !mostrarBusqueda
                    }
                )
            }

            items(productosFiltrados, key = { it.id }) { product ->
                val recetasVinculadas = recetas.filter { it.productosAsociadosIds.contains(product.id) }
                val expandido = productoExpandidoId == product.id

                ProductCatalogCard(
                    product = product,
                    expanded = expandido,
                    linkedRecipes = recetasVinculadas,
                    onToggleExpand = {
                        productoExpandidoId = if (expandido) null else product.id
                    },
                    onOpenRecipe = { recetaId ->
                        viewModel.navigateTo(Screen.RecetaDetalle(recetaId))
                    }
                )
            }
        }

        FloatingBottomBar(
            selectedRoute = Screen.Profile.route,
            onNavigate = { screen -> viewModel.navigateBottomTab(screen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ProductCatalogCard(
    product: ProductItem,
    expanded: Boolean,
    linkedRecipes: List<RecetaOficial>,
    onToggleExpand: () -> Unit,
    onOpenRecipe: (Int) -> Unit
) {
    Surface(
        onClick = onToggleExpand,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.68f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (product.imagenRes != 0) {
                Image(
                    painter = painterResource(id = product.imagenRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MartiniDarkKhaki
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Contraer ficha" else "Ver ficha y recetas",
                    tint = MartiniGreen
                )
            }

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
            )

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Ficha técnica",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "• Presentación: ${product.formato}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "• Uso recomendado: ${product.aplicacion}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }

                    if (linkedRecipes.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Recetas asociadas (${linkedRecipes.size})",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            linkedRecipes.forEach { receta ->
                                Surface(
                                    onClick = { onOpenRecipe(receta.id) },
                                    shape = RoundedCornerShape(16.dp),
                                    color = MartiniGreenContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 11.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = receta.titulo,
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                                color = MartiniGreenDark
                                            )
                                            Text(
                                                text = "${receta.categoria} • ${receta.tiempo}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MartiniGreenDark.copy(alpha = 0.75f)
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "Abrir receta",
                                            tint = MartiniGreen,
                                            modifier = Modifier.size(17.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AppMasterMartini_Grupo9Theme {
        ProfileScreen()
    }
}
