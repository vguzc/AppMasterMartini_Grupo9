package com.example.appmastermartini_grupo9.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.model.RecetaOficial
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.MartiniTopHeader
import com.example.appmastermartini_grupo9.ui.components.PinterestCategoryTabs
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel = viewModel()) {
    val recetas by viewModel.recetasOficiales.collectAsState()
    val guardadasIds by viewModel.recetasGuardadasIds.collectAsState()
    val destacada = recetas.firstOrNull { it.esOficialMartini } ?: recetas.firstOrNull()
    val abrirRecetas = { viewModel.navigateBottomTab(Screen.Recetas) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MartiniTopHeader(title = "Descubre")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AccesoDescubre("Recetas", Icons.AutoMirrored.Filled.MenuBook, abrirRecetas, Modifier.weight(1f))
                AccesoDescubre(
                    label = "Cursos",
                    icon = Icons.Default.School,
                    onClick = { viewModel.navigateBottomTab(Screen.Animaciones) },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AccesoDescubre(
                    label = "Productos",
                    icon = Icons.Default.GridView,
                    onClick = { viewModel.navigateBottomTab(Screen.Profile) },
                    modifier = Modifier.weight(1f)
                )
                AccesoDescubre(
                    label = "Mi perfil",
                    icon = Icons.Default.Person,
                    onClick = { viewModel.navigateBottomTab(Screen.CamaraFoto) },
                    modifier = Modifier.weight(1f)
                )
            }

            destacada?.let { receta ->
                Text("Receta destacada", style = MaterialTheme.typography.titleLarge)
                TarjetaReceta(
                    receta = receta,
                    esGuardada = receta.id in guardadasIds,
                    onToggleGuardar = { viewModel.toggleGuardarReceta(receta.id) },
                    onClick = { viewModel.navigateTo(Screen.RecetaDetalle(receta.id)) },
                    modifier = Modifier.fillMaxWidth().aspectRatio(1.7f)
                )
                OutlinedButton(
                    onClick = abrirRecetas,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                ) {
                    Text("Ver todas las recetas")
                }
            }
        }
    }
}

@Composable
private fun AccesoDescubre(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 108.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RecetasScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val recetas by viewModel.recetasOficiales.collectAsState()
    val guardadasIds by viewModel.recetasGuardadasIds.collectAsState()
    var mostrarBusqueda by rememberSaveable { mutableStateOf(false) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var categoriaSeleccionada by rememberSaveable { mutableStateOf("Todos") }

    val categorias = listOf("Todos", "Oficiales", "Pastelería", "Chocolatería", "Bollería", "Gelato", "Salados")
    val recetasFiltradas = recetas.filter { receta ->
        val coincideCategoria = when (categoriaSeleccionada) {
            "Todos" -> true
            "Oficiales" -> receta.esOficialMartini
            else -> receta.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        }
        val coincideTexto = busqueda.isBlank() ||
            receta.titulo.contains(busqueda, ignoreCase = true) ||
            receta.subtitulo.contains(busqueda, ignoreCase = true) ||
            receta.autor.contains(busqueda, ignoreCase = true) ||
            receta.ingredientes.any { it.contains(busqueda, ignoreCase = true) }
        coincideCategoria && coincideTexto
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        val alturaImagen = (maxHeight * 0.42f).coerceIn(140.dp, 360.dp)

        LazyColumn(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MartiniTopHeader(title = "Recetas", onBackClick = onBack)
            }
            item {
                Button(
                    onClick = { viewModel.navigateTo(Screen.CrearReceta) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(
                        "Añadir receta",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            item {
                PinterestCategoryTabs(
                    tabs = categorias,
                    selectedTab = categoriaSeleccionada,
                    onSelectTab = { categoriaSeleccionada = it },
                    searchQuery = busqueda,
                    onSearchQueryChange = { busqueda = it },
                    searchPlaceholder = "Buscar recetas",
                    searchVisible = mostrarBusqueda,
                    onToggleSearch = {
                        if (mostrarBusqueda) busqueda = ""
                        mostrarBusqueda = !mostrarBusqueda
                    }
                )
            }
            if (recetasFiltradas.isEmpty()) {
                item {
                    Text("Sin recetas para mostrar", style = MaterialTheme.typography.titleMedium)
                    Text("Prueba otra categoría o término de búsqueda.")
                }
            }
            items(recetasFiltradas, key = { it.id }) { receta ->
                TarjetaReceta(
                    receta = receta,
                    esGuardada = receta.id in guardadasIds,
                    onToggleGuardar = { viewModel.toggleGuardarReceta(receta.id) },
                    onClick = { viewModel.navigateTo(Screen.RecetaDetalle(receta.id)) },
                    modifier = Modifier.fillMaxWidth().height(alturaImagen)
                )
            }
        }
    }
}

@Composable
private fun TarjetaReceta(
    receta: RecetaOficial,
    esGuardada: Boolean,
    onToggleGuardar: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier
) {
    TarjetaVistaReceta(
        titulo = receta.titulo,
        detalle = if (receta.esOficialMartini) "Receta oficial Master Martini" else receta.autor,
        imagenRes = receta.imagenRes,
        imagenUri = receta.imagenUri,
        onClick = onClick,
        modifier = modifier,
        sobreImagen = {
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface
            ) {
                IconButton(onClick = onToggleGuardar, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = if (esGuardada) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (esGuardada) "Quitar de guardados" else "Guardar receta"
                    )
                }
            }
        }
    )
}

@Composable
internal fun TarjetaVistaReceta(
    titulo: String,
    detalle: String,
    imagenRes: Int,
    imagenUri: Uri?,
    onClick: () -> Unit,
    modifier: Modifier,
    sobreImagen: @Composable BoxScope.() -> Unit = {},
    acciones: @Composable () -> Unit = {}
) {
    Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Box(modifier = modifier) {
            val imagenLocal = painterResource(id = imagenRes)
            if (imagenUri != null) {
                AsyncImage(
                    model = imagenUri,
                    contentDescription = null,
                    placeholder = imagenLocal,
                    error = imagenLocal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = imagenLocal,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            sobreImagen()
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(detalle, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text("Ver receta")
            }
            acciones()
        }
    }
}
