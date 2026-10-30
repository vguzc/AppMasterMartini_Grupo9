package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.MartiniVideoPlayer
import com.example.appmastermartini_grupo9.ui.theme.MartiniCharcoal
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel

/**
 * =========================================================================
 * DETALLE DE RECETA OFICIAL (PLAN UX/UI PÁGINA 6)
 * - Prioriza fotografía, ingredientes, preparación, tiempo, nivel y
 *   productos Master Martini asociados.
 * - Al final incorpora la sección contextual "Conversación", mostrando
 *   el número de comentarios y permitiendo abrir el hilo en Comunidad
 *   sin forzar redirecciones automáticas.
 * =========================================================================
 */
@Composable
fun RecetaDetalleScreen(
    recetaId: Int,
    viewModel: MainViewModel = viewModel(),
    onBack: () -> Unit = { viewModel.navigateBack() }
) {
    val recetas by viewModel.recetasOficiales.collectAsState()
    val guardadasIds by viewModel.recetasGuardadasIds.collectAsState()
    val publicaciones by viewModel.publicacionesComunidad.collectAsState()
    val nombreUsuarioActual by viewModel.nombreUsuario.collectAsState()

    val receta = recetas.find { it.id == recetaId } ?: recetas.firstOrNull() ?: return
    val esGuardada = guardadasIds.contains(receta.id)
    val hiloAsociado = publicaciones.find { it.recetaOficialId == receta.id }
    val listaComentarios = hiloAsociado?.comentarios ?: emptyList()
    var nuevoComentarioTexto by remember { mutableStateOf("") }

    val productosAsociados = remember(receta.id) {
        viewModel.catalogoProductos.filter { receta.productosAsociadosIds.contains(it.id) }
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val tieneVideo = receta.videoRes != 0 || receta.videoUri != null

    val ingredientesMarcados = remember(receta.id) { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        // 1. Cabecera multimedia: En video ocupa pantalla completa (100% de la altura); en foto 320.dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (tieneVideo) screenHeight else 320.dp)
        ) {
            if (tieneVideo) {
                MartiniVideoPlayer(
                    videoRes = receta.videoRes,
                    videoUri = receta.videoUri,
                    autoPlay = true,
                    initialMuted = false,
                    contentDescription = receta.titulo,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (receta.imagenUri != null) {
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

            // Degradado superior para botones de volver y guardar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (tieneVideo) 150.dp else 120.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.55f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Barra superior flotante (Volver y Guardar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.92f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MartiniCharcoal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Surface(
                    onClick = { viewModel.toggleGuardarReceta(receta.id) },
                    shape = RoundedCornerShape(50),
                    color = if (esGuardada) MartiniGreen else Color.White.copy(alpha = 0.92f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (esGuardada) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar receta",
                            tint = if (esGuardada) Color.White else MartiniCharcoal,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            text = if (esGuardada) "Guardada" else "Guardar",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.sp
                            ),
                            color = if (esGuardada) Color.White else MartiniCharcoal
                        )
                    }
                }
            }

            // Si es video, overlay inferior informativo con título y sugerencia de deslizar
            if (tieneVideo) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.40f),
                                    Color.Black.copy(alpha = 0.88f)
                                )
                            )
                        )
                        .padding(horizontal = 22.dp, vertical = 24.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MartiniGreen
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Video en HD",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.22f)
                            ) {
                                Text(
                                    text = receta.categoria,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = receta.titulo,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                lineHeight = 30.sp
                            ),
                            color = Color.White
                        )

                        Text(
                            text = receta.subtitulo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.88f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Indicador "Baja para ver la receta"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.20f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Baja para ver la receta e ingredientes",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Cuerpo editorial de la receta
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 22.dp, vertical = 22.dp)),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Categoría + Autoría (Oficial verificada o Usuario) + Título + Descripción
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = receta.colorCategoriaBg
                    ) {
                        Text(
                            text = receta.categoria,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.4.sp
                            ),
                            color = receta.colorCategoriaTexto,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    val esDonSacoRoto = receta.autor.equals("Don Saco Roto", ignoreCase = true)
                    val esMiPropiaReceta = receta.autor.equals(nombreUsuarioActual, ignoreCase = true) || receta.esPropiaUsuario

                    Surface(
                        onClick = {
                            if (receta.esOficialMartini) {
                                viewModel.navigateTo(Screen.Profile)
                            } else if (esDonSacoRoto) {
                                // Abre el perfil propio de Don Saco Roto (con su foto comiendo nachos)
                                viewModel.navigateTo(Screen.PerfilCreador("Don Saco Roto"))
                            } else if (esMiPropiaReceta) {
                                // Abre mi propio perfil (Valentina Soto)
                                viewModel.navigateTo(Screen.CamaraFoto)
                            } else {
                                viewModel.navigateTo(Screen.PerfilCreador(receta.autor))
                            }
                        },
                        shape = RoundedCornerShape(50),
                        color = if (receta.esOficialMartini) {
                            MartiniGreenContainer.copy(alpha = 0.72f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (receta.esOficialMartini) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = MartiniGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Master Martini Oficial",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.sp
                                    ),
                                    color = MartiniGreenDark
                                )
                            } else {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(MartiniGreenContainer)
                                ) {
                                    if (esDonSacoRoto) {
                                        Image(
                                            painter = painterResource(id = R.drawable.avatar_don_saco_roto),
                                            contentDescription = "Don Saco Roto",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Text(
                                            text = (receta.autor.ifBlank { "M" }).take(1).uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MartiniGreen
                                        )
                                    }
                                }
                                Text(
                                    text = receta.autor,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Ver perfil",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = receta.titulo,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = receta.subtitulo,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f)
                )
            }

            // Ficha rápida: Tiempo · Nivel · Porciones
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RecipeMetricItem(
                        icon = Icons.Default.Schedule,
                        label = "Tiempo",
                        value = receta.tiempo
                    )
                    RecipeMetricItem(
                        icon = Icons.Default.Tune,
                        label = "Nivel",
                        value = receta.nivel
                    )
                    RecipeMetricItem(
                        icon = Icons.Default.Restaurant,
                        label = "Rendimiento",
                        value = receta.porciones
                    )
                }
            }

            // Productos Master Martini utilizados (enlace contextual con Productos)
            if (productosAsociados.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Productos Master Martini utilizados",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    productosAsociados.forEach { prod ->
                        Surface(
                            onClick = { viewModel.abrirProductoEnCatalogo(prod.id) },
                            shape = RoundedCornerShape(18.dp),
                            color = MartiniGreenContainer.copy(alpha = 0.42f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = prod.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${prod.tag} • Ver ficha técnica",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MartiniDarkKhaki
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Ver producto",
                                    tint = MartiniGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))

            // Ingredientes interactivos
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Ingredientes",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                receta.ingredientes.forEachIndexed { index, ing ->
                    val marcado = ingredientesMarcados.contains(index)
                    Surface(
                        onClick = {
                            if (marcado) ingredientesMarcados.remove(index) else ingredientesMarcados.add(index)
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = if (marcado) {
                            MartiniGreenContainer.copy(alpha = 0.35f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (marcado) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (marcado) MartiniGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = ing,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (marcado) 0.65f else 0.92f)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))

            // Pasos de preparación
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Preparación paso a paso",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                receta.pasos.forEachIndexed { idx, paso ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MartiniGreen)
                        ) {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.sp
                                ),
                                color = Color.White
                            )
                        }
                        Text(
                            text = paso,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.88f),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3. Sección de Comentarios en tiempo real (directa en la receta)
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = MartiniGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Comentarios (${listaComentarios.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    if (listaComentarios.isEmpty()) {
                        Text(
                            text = "Aún no hay comentarios sobre esta receta. ¡Sé el primero en dejar tus dudas, tips o resultados!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            listaComentarios.forEach { comentario ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                val esDonSacoRoto = comentario.autor.equals("Don Saco Roto", ignoreCase = true)
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape)
                                                        .background(if (comentario.esOficialMartini) MartiniGreen else MartiniGreenContainer)
                                                ) {
                                                    if (esDonSacoRoto) {
                                                        Image(
                                                            painter = painterResource(id = R.drawable.avatar_don_saco_roto),
                                                            contentDescription = "Don Saco Roto",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize()
                                                        )
                                                    } else {
                                                        Text(
                                                            text = (comentario.autor.ifBlank { "M" }).take(1).uppercase(),
                                                            style = MaterialTheme.typography.labelSmall.copy(
                                                                fontSize = 11.sp,
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            color = if (comentario.esOficialMartini) Color.White else MartiniGreen
                                                        )
                                                    }
                                                }
                                                Text(
                                                    text = comentario.autor,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                if (comentario.esOficialMartini) {
                                                    Icon(
                                                        imageVector = Icons.Default.Verified,
                                                        contentDescription = "Oficial",
                                                        tint = MartiniGreen,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = comentario.haceCuanto,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                                            )
                                        }
                                        Text(
                                            text = comentario.texto,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Campo para escribir y enviar un comentario directamente
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = nuevoComentarioTexto,
                            onValueChange = { nuevoComentarioTexto = it },
                            placeholder = {
                                Text(
                                    text = "Escribe un comentario o duda...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                if (nuevoComentarioTexto.isNotBlank()) {
                                    viewModel.agregarComentarioAReceta(receta.id, nuevoComentarioTexto)
                                    nuevoComentarioTexto = ""
                                }
                            },
                            enabled = nuevoComentarioTexto.isNotBlank()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (nuevoComentarioTexto.isNotBlank()) MartiniGreen else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Enviar comentario",
                                        tint = if (nuevoComentarioTexto.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun RecipeMetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MartiniGreen,
            modifier = Modifier.size(19.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
