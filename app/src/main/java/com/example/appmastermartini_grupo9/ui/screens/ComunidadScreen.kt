package com.example.appmastermartini_grupo9.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.model.ComentarioComunidad
import com.example.appmastermartini_grupo9.model.PublicacionComunidad
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.FloatingBottomBar
import com.example.appmastermartini_grupo9.ui.components.MartiniProfileAvatar
import com.example.appmastermartini_grupo9.ui.components.MartiniTopHeader
import com.example.appmastermartini_grupo9.ui.components.PinterestCategoryTabs
import com.example.appmastermartini_grupo9.ui.theme.MartiniCrimson
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import kotlinx.coroutines.launch

/**
 * =========================================================================
 * D · COMUNIDAD — FORO DE PASTELEROS CON ESTÉTICA MASTER MARTINI Y LÓGICA THREADS
 * - Hilos continuos con línea vertical de conversación.
 * - Barra de acciones oficial: Like, Comentar y Compartir.
 * - Creador rápido de hilos (Threads Composer) al tope del feed.
 * - Pestañas "Para ti", "Siguiendo" y filtros temáticos de obrador.
 * =========================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunidadScreen(
    viewModel: MainViewModel = viewModel(),
    avatarUri: Uri? = null
) {
    val context = LocalContext.current
    val nombreUsuarioActual by viewModel.nombreUsuario.collectAsState()
    val publicaciones by viewModel.publicacionesComunidad.collectAsState()
    val recetas by viewModel.recetasOficiales.collectAsState()
    val likeadosIds by viewModel.postsLikeadosIds.collectAsState()
    val guardadosIds by viewModel.postsGuardadosIds.collectAsState()

    var mostrarBusqueda by rememberSaveable { mutableStateOf(false) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var filtroActivo by rememberSaveable { mutableStateOf("Para ti") }

    // BottomSheet para crear un nuevo hilo estilo Threads
    var mostrarComposerThreads by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Pestañas de hilos estilo Threads (Para ti / Siguiendo)
    val filtros = listOf("Para ti", "Siguiendo")

    val postsFiltrados = publicaciones.filter { post ->
        val coincideFiltro = when (filtroActivo) {
            "Siguiendo" -> {
                // Autores seguidos de la comunidad: Don Saco Roto, Chef Matteo Rossi, o el usuario activo
                post.autor.equals("Don Saco Roto", ignoreCase = true) ||
                post.autor.equals("Chef Matteo Rossi", ignoreCase = true) ||
                (nombreUsuarioActual.isNotBlank() && post.autor.equals(nombreUsuarioActual, ignoreCase = true))
            }
            else -> true
        }

        val coincideTexto = busqueda.isBlank() ||
            post.titulo.contains(busqueda, ignoreCase = true) ||
            post.autor.contains(busqueda, ignoreCase = true) ||
            post.descripcion.contains(busqueda, ignoreCase = true)

        coincideFiltro && coincideTexto
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
                top = 10.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cabecera superior unificada
            item {
                MartiniTopHeader(
                    title = "Comunidad",
                    onCreateClick = { mostrarComposerThreads = true },
                    avatarUri = avatarUri,
                    onAvatarClick = { viewModel.navigateTo(Screen.CamaraFoto) }
                )
            }

            // Pestañas de canales de hilo estilo Threads / Pinterest
            item {
                PinterestCategoryTabs(
                    tabs = filtros,
                    selectedTab = filtroActivo,
                    onSelectTab = { filtroActivo = it },
                    searchQuery = busqueda,
                    onSearchQueryChange = { busqueda = it },
                    searchPlaceholder = "Buscar en hilos, consultas o insumos...",
                    searchVisible = mostrarBusqueda,
                    onToggleSearch = {
                        if (mostrarBusqueda) busqueda = ""
                        mostrarBusqueda = !mostrarBusqueda
                    }
                )
            }

            // Barra rápida de inicio de hilo ("Inicia un hilo..." estilo Threads)
            item {
                ThreadsQuickComposerBar(
                    avatarUri = avatarUri,
                    onClick = { mostrarComposerThreads = true }
                )
            }

            // Lista de Hilos (Feed estilo Threads)
            if (postsFiltrados.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Sin hilos en esta categoría",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Inicia el primer hilo para consultar a otros pasteleros o compartir un tip de obrador.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                    }
                }
            } else {
                items(postsFiltrados, key = { it.id }) { post ->
                    val recetaVinculada = post.recetaOficialId?.let { id ->
                        recetas.find { it.id == id }
                    }

                    ThreadsPostCard(
                        post = post,
                        avatarUriUsuario = avatarUri,
                        nombreRecetaVinculada = recetaVinculada?.titulo,
                        isLiked = likeadosIds.contains(post.id),
                        isSaved = guardadosIds.contains(post.id),
                        onToggleLike = { viewModel.toggleLikePost(post.id) },
                        onToggleSave = {
                            viewModel.toggleGuardarPost(post.id)
                            val mensaje = if (guardadosIds.contains(post.id)) "Hilo eliminado de guardados" else "Hilo guardado"
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            compartirHilo(context, post.titulo, post.autor, post.descripcion)
                        },
                        onOpenPost = { viewModel.navigateTo(Screen.PostDetalle(post.id)) },
                        onOpenReceta = post.recetaOficialId?.let { recId ->
                            { viewModel.navigateTo(Screen.RecetaDetalle(recId)) }
                        },
                        onOpenAuthor = {
                            if (post.autor.equals("Don Saco Roto", ignoreCase = true)) {
                                viewModel.navigateTo(Screen.PerfilCreador("Don Saco Roto"))
                            } else if (post.esPropiaUsuario) {
                                viewModel.navigateTo(Screen.CamaraFoto)
                            } else if (!post.esOficialMartini) {
                                viewModel.navigateTo(Screen.PerfilCreador(post.autor))
                            }
                        }
                    )
                }
            }
        }

        // Barra flotante inferior de 4 botones
        FloatingBottomBar(
            selectedRoute = Screen.Comunidad.route,
            onNavigate = { screen -> viewModel.navigateBottomTab(screen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Modal BottomSheet estilo Threads para redactar un hilo nuevo
        if (mostrarComposerThreads) {
            ModalBottomSheet(
                onDismissRequest = { mostrarComposerThreads = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                ThreadsComposerSheet(
                    avatarUri = avatarUri,
                    nombreUsuario = nombreUsuarioActual,
                    onPublicar = { titulo, descripcion, fotoUri ->
                        viewModel.publicarEnForoComunidad(
                            tipoPublicacion = "Comunidad",
                            titulo = titulo,
                            descripcion = descripcion,
                            productosSeleccionados = emptyList(),
                            recetaAsociadaId = null,
                            imagenUri = fotoUri,
                            imagenRes = 0
                        )
                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                            mostrarComposerThreads = false
                            Toast.makeText(context, "¡Hilo publicado con éxito!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onCancelar = {
                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                            mostrarComposerThreads = false
                        }
                    }
                )
            }
        }
    }
}

/**
 * Barra rápida contextual de Threads en la parte superior: "Inicia un hilo..."
 */
@Composable
private fun ThreadsQuickComposerBar(
    avatarUri: Uri?,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MartiniProfileAvatar(
                avatarUri = avatarUri,
                size = 38.dp
            )

            Text(
                text = "¿Duda con el templado o tip de obrador? Iniciar hilo...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = MartiniGreen
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Hilo",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta de Hilo con Arquitectura de 2 Columnas (Estética Master Martini + Lógica Threads):
 * - Columna Izquierda: Avatar del autor + Línea vertical continua que conecta con las respuestas.
 * - Columna Derecha: Cabecera con handle, tiempo y badge; contenido del hilo; media/receta;
 *   y la Barra de Acciones de Threads (Like, Reply, Share, Bookmark).
 */
@Composable
private fun ThreadsPostCard(
    post: PublicacionComunidad,
    avatarUriUsuario: Uri?,
    nombreRecetaVinculada: String?,
    isLiked: Boolean,
    isSaved: Boolean,
    onToggleLike: () -> Unit,
    onToggleSave: () -> Unit,
    onShare: () -> Unit,
    onOpenPost: () -> Unit,
    onOpenReceta: (() -> Unit)?,
    onOpenAuthor: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val likesTotales = post.likesBase + if (isLiked) 1 else 0
    val tieneFoto = post.imagenUri != null || post.imagenRes != 0

    var menuOpcionesAbierto by remember { mutableStateOf(false) }

    Surface(
        onClick = onOpenPost,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // =========================================================================
            // COLUMNA IZQUIERDA: Avatar + Línea Continua de Hilo (Signature Threads)
            // =========================================================================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight()
            ) {
                // Avatar del Autor
                AuthorAvatar(
                    post = post,
                    avatarUriUsuario = avatarUriUsuario,
                    size = 40.dp,
                    onClick = onOpenAuthor
                )

                // Línea vertical que desciende hacia las respuestas
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .padding(vertical = 4.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
                            shape = CircleShape
                        )
                )

                // Final de línea de hilo: Mini avatars de quienes respondieron (Facepile)
                if (post.comentarios.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(MartiniGreenContainer.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${post.comentarios.size}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MartiniGreenDark
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    )
                }
            }

            // =========================================================================
            // COLUMNA DERECHA: Contenido del Hilo y Barra Oficial de Acciones Threads
            // =========================================================================
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Fila Superior: Autor, @handle, tiempo, Categoría y Menú de tres puntos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = post.autor,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = if (onOpenAuthor != null) Modifier.clickable { onOpenAuthor() } else Modifier
                        )

                        if (post.esOficialMartini) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Oficial Master Martini",
                                tint = MartiniGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = "• ${post.haceCuanto}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                            maxLines = 1
                        )
                    }

                    // Menú contextual de 3 puntos horizontales (Threads style)
                    Box {
                        IconButton(
                            onClick = { menuOpcionesAbierto = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "Opciones del hilo",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = menuOpcionesAbierto,
                            onDismissRequest = { menuOpcionesAbierto = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Copiar enlace del hilo") },
                                onClick = {
                                    menuOpcionesAbierto = false
                                    Toast.makeText(context, "Enlace copiado al portapapeles", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (isSaved) "Eliminar de guardados" else "Guardar hilo") },
                                onClick = {
                                    menuOpcionesAbierto = false
                                    onToggleSave()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Compartir hilo...") },
                                onClick = {
                                    menuOpcionesAbierto = false
                                    onShare()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Silenciar a ${post.autor}") },
                                onClick = {
                                    menuOpcionesAbierto = false
                                    Toast.makeText(context, "Has silenciado a ${post.autor}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }

                // Título y cuerpo del hilo (Click directo para abrir el hilo)
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenPost() }
                ) {
                    Text(
                        text = post.titulo,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.5.sp,
                            lineHeight = 21.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = post.descripcion,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Adjunto multimedia (foto de "Así me quedó" o consulta técnica)
                if (tieneFoto) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        if (post.imagenUri != null) {
                            AsyncImage(
                                model = post.imagenUri,
                                contentDescription = post.titulo,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = post.imagenRes),
                                contentDescription = post.titulo,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                // Receta citada de Descubre (Quote Post Embed estilo Threads)
                if (nombreRecetaVinculada != null && onOpenReceta != null) {
                    Surface(
                        onClick = onOpenReceta,
                        shape = RoundedCornerShape(14.dp),
                        color = MartiniGreenContainer.copy(alpha = 0.42f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MartiniGreen.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = MartiniGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Receta de Descubre",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MartiniGreenDark
                                )
                                Text(
                                    text = nombreRecetaVinculada,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "Ver →",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MartiniGreen
                            )
                        }
                    }
                }

                // =========================================================================
                // BARRA OFICIAL DE ACCIONES DE THREADS (4 BOTONES ALINEADOS) + GUARDAR
                // =========================================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1. Like (Corazón)
                        ActionIconButton(
                            icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Me gusta",
                            count = likesTotales,
                            tint = if (isLiked) MartiniCrimson else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            onClick = onToggleLike
                        )

                        // 2. Reply (Globo de diálogo)
                        ActionIconButton(
                            icon = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Respuestas",
                            count = post.comentarios.size,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            onClick = onOpenPost
                        )

                        // 3. Compartir / Enviar (Avión de papel)
                        IconButton(
                            onClick = onShare,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Guardar hilo (Bookmark)
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = if (isSaved) MartiniGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                // Resumen social en la base del hilo (Threads Activity String)
                Text(
                    text = "${post.comentarios.size} respuestas • $likesTotales me gusta",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}

/**
 * Botón interactivo de acción de Threads con contador numérico.
 */
@Composable
private fun ActionIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    count: Int,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
        if (count > 0) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = tint
            )
        }
    }
}

/**
 * Avatar adaptable según el autor del hilo.
 */
@Composable
private fun AuthorAvatar(
    post: PublicacionComunidad,
    avatarUriUsuario: Uri?,
    size: androidx.compose.ui.unit.Dp,
    onClick: (() -> Unit)? = null
) {
    val modifier = Modifier
        .size(size)
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)

    if (post.autor.equals("Don Saco Roto", ignoreCase = true)) {
        Image(
            painter = painterResource(id = R.drawable.avatar_don_saco_roto),
            contentDescription = "Don Saco Roto",
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(CircleShape)
        )
    } else if (post.esPropiaUsuario) {
        MartiniProfileAvatar(
            avatarUri = avatarUriUsuario,
            size = size,
            modifier = modifier
        )
    } else {
        Surface(
            shape = CircleShape,
            color = if (post.esOficialMartini) MartiniGreen else MartiniDarkKhaki,
            modifier = modifier
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = post.autor.take(1).uppercase(),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

/**
 * =========================================================================
 * MODAL BOTTOMSHEET: COMPOSER DE HILOS ESTILO THREADS
 * =========================================================================
 */
@Composable
private fun ThreadsComposerSheet(
    avatarUri: Uri?,
    nombreUsuario: String = "Pastelero",
    onPublicar: (titulo: String, descripcion: String, fotoUri: Uri?) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    var fotoSeleccionadaUri by remember { mutableStateOf<Uri?>(null) }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            fotoSeleccionadaUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cabecera del Composer: Cancelar y botón Publicar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cancelar",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.clickable { onCancelar() }
            )

            Text(
                text = "Nuevo hilo",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                onClick = {
                    if (titulo.isNotBlank() && contenido.isNotBlank()) {
                        onPublicar(titulo, contenido, fotoSeleccionadaUri)
                    }
                },
                enabled = titulo.isNotBlank() && contenido.isNotBlank(),
                shape = RoundedCornerShape(50),
                color = if (titulo.isNotBlank() && contenido.isNotBlank()) MartiniGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "Publicar",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (titulo.isNotBlank() && contenido.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

        // Fila de Autor con avatar y campos de texto con línea vertical de hilo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(40.dp)
            ) {
                MartiniProfileAvatar(
                    avatarUri = avatarUri,
                    size = 40.dp
                )
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(130.dp)
                        .padding(vertical = 4.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = nombreUsuario,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    placeholder = { Text("Título de tu consulta o tip...") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MartiniGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    placeholder = { Text("Describe el proceso, temperaturas, batido o rendimiento...") },
                    minLines = 3,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MartiniGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Subir foto / Preview de foto adjunta
                if (fotoSeleccionadaUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        AsyncImage(
                            model = fotoSeleccionadaUri,
                            contentDescription = "Foto adjunta",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            onClick = { fotoSeleccionadaUri = null },
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.65f),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar foto",
                                tint = Color.White,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                } else {
                    Surface(
                        onClick = { galeriaLauncher.launch("image/*") },
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = "Subir foto",
                                tint = MartiniGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Subir foto (opcional)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

/**
 * =========================================================================
 * VISTA COMPLETA DEL HILO (DETALLE DE CONVERSACIÓN ESTILO THREADS)
 * - Muestra el post padre conectado mediante la línea continua vertical
 *   con todas las respuestas secundarias y el editor fijo de respuesta al fondo.
 * =========================================================================
 */
@Composable
fun PostDetalleScreen(
    postId: Int,
    viewModel: MainViewModel = viewModel(),
    avatarUriUsuario: Uri? = null,
    onBack: () -> Unit = { viewModel.navigateBack() }
) {
    val context = LocalContext.current
    val publicaciones by viewModel.publicacionesComunidad.collectAsState()
    val recetas by viewModel.recetasOficiales.collectAsState()
    val likeadosIds by viewModel.postsLikeadosIds.collectAsState()
    val guardadosIds by viewModel.postsGuardadosIds.collectAsState()
    val comentariosLikeadosIds by viewModel.comentariosLikeadosIds.collectAsState()

    val post = publicaciones.find { it.id == postId } ?: publicaciones.firstOrNull() ?: return
    val isLiked = likeadosIds.contains(post.id)
    val isSaved = guardadosIds.contains(post.id)
    val tieneFoto = post.imagenUri != null || post.imagenRes != 0
    val recetaVinculada = post.recetaOficialId?.let { id -> recetas.find { it.id == id } }

    val likesTotales = post.likesBase + if (isLiked) 1 else 0

    var nuevoComentario by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Barra Superior Limpia de Hilo (Threads Top Bar)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    onClick = onBack,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = "Hilo",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    onClick = {
                        compartirHilo(context, post.titulo, post.autor, post.descripcion)
                    },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir hilo",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Surface(
                    onClick = { viewModel.toggleGuardarPost(post.id) },
                    shape = CircleShape,
                    color = if (isSaved) MartiniGreen else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = if (isSaved) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // =========================================================================
            // POST PRINCIPAL (RAÍZ DEL HILO)
            // =========================================================================
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Cabecera del Autor
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            AuthorAvatar(
                                post = post,
                                avatarUriUsuario = avatarUriUsuario,
                                size = 46.dp
                            )
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = post.autor,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    if (post.esOficialMartini) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = MartiniGreen,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${post.rolAutor} • ${post.haceCuanto}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    // Título y contenido
                    Text(
                        text = post.titulo,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = post.descripcion,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.5.sp,
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.88f)
                    )

                    // Imagen
                    if (tieneFoto) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .clip(RoundedCornerShape(20.dp))
                        ) {
                            if (post.imagenUri != null) {
                                AsyncImage(
                                    model = post.imagenUri,
                                    contentDescription = post.titulo,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = post.imagenRes),
                                    contentDescription = post.titulo,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    // Receta vinculada
                    if (recetaVinculada != null) {
                        Surface(
                            onClick = { viewModel.navigateTo(Screen.RecetaDetalle(recetaVinculada.id)) },
                            shape = RoundedCornerShape(16.dp),
                            color = MartiniGreenContainer.copy(alpha = 0.45f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MartiniGreen.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = null,
                                    tint = MartiniGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Ver receta paso a paso en Descubre",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MartiniGreenDark
                                    )
                                    Text(
                                        text = recetaVinculada.titulo,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = "Abrir →",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MartiniGreen
                                )
                            }
                        }
                    }

                    // Resumen numérico estilo Threads
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$likesTotales Me gusta • ${post.comentarios.size} Respuestas",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        )
                    }

                    // Fila de acciones (Like, Comentar, Compartir)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActionIconButton(
                            icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Me gusta",
                            count = likesTotales,
                            tint = if (isLiked) MartiniCrimson else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                            onClick = { viewModel.toggleLikePost(post.id) }
                        )

                        ActionIconButton(
                            icon = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Responder",
                            count = post.comentarios.size,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                            onClick = { /* Foco en el campo de texto inferior */ }
                        )

                        IconButton(
                            onClick = { compartirHilo(context, post.titulo, post.autor, post.descripcion) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir",
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }

            // =========================================================================
            // CADENA DE RESPUESTAS CONECTADAS POR LA LÍNEA DEL HILO (THREADS REPLIES)
            // =========================================================================
            item {
                Text(
                    text = "Respuestas en el hilo",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (post.comentarios.isEmpty()) {
                item {
                    Text(
                        text = "Sé el primero en responder este hilo técnico o compartir tu experiencia.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
                    )
                }
            } else {
                items(post.comentarios, key = { it.id }) { com ->
                    val claveComentario = "${post.id}-${com.id}"
                    val comLiked = comentariosLikeadosIds.contains(claveComentario)

                    ThreadsReplyRow(
                        comentario = com,
                        isLiked = comLiked,
                        onToggleLike = { viewModel.toggleLikeComentario(post.id, com.id) },
                        onReplyToAuthor = {
                            nuevoComentario = "@${com.autor.replace(" ", "")} "
                        }
                    )
                }
            }
        }

        // =========================================================================
        // BARRA FIJA INFERIOR DE RESPUESTA ESTILO THREADS
        // =========================================================================
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Avatar del usuario que responde (Valentina Soto)
                MartiniProfileAvatar(
                    avatarUri = avatarUriUsuario,
                    size = 36.dp
                )

                TextField(
                    value = nuevoComentario,
                    onValueChange = { nuevoComentario = it },
                    placeholder = {
                        Text(
                            text = "Responder a ${post.autor}...",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MartiniGreen
                    ),
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    onClick = {
                        if (nuevoComentario.isNotBlank()) {
                            viewModel.agregarComentario(post.id, nuevoComentario)
                            nuevoComentario = ""
                            coroutineScope.launch {
                                listState.animateScrollToItem(post.comentarios.size + 2)
                            }
                        }
                    },
                    enabled = nuevoComentario.isNotBlank(),
                    shape = CircleShape,
                    color = if (nuevoComentario.isNotBlank()) MartiniGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar respuesta",
                            tint = if (nuevoComentario.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fila de respuesta dentro del hilo conectada a la línea vertical.
 */
@Composable
private fun ThreadsReplyRow(
    comentario: ComentarioComunidad,
    isLiked: Boolean,
    onToggleLike: () -> Unit,
    onReplyToAuthor: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Columna izquierda: Avatar y línea continua del hilo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(36.dp)
                .fillMaxHeight()
        ) {
            Surface(
                shape = CircleShape,
                color = if (comentario.esOficialMartini) MartiniGreen else MartiniDarkKhaki,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = comentario.autor.take(1).uppercase(),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
                    .padding(vertical = 4.dp)
                    .background(
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            )
        }

        // Columna derecha: Mensaje de respuesta
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = comentario.autor,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (comentario.esOficialMartini) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Asesor Oficial Master Martini",
                            tint = MartiniGreen,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                Text(
                    text = comentario.haceCuanto,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Text(
                text = comentario.texto,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f)
            )

            // Acciones de la respuesta (Me gusta y responderle)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onToggleLike() }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Me gusta respuesta",
                        tint = if (isLiked) MartiniCrimson else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = if (isLiked) "1" else "0",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = if (isLiked) MartiniCrimson else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Text(
                    text = "Responder",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MartiniGreen
                    ),
                    modifier = Modifier.clickable { onReplyToAuthor() }
                )
            }
        }
    }
}

/**
 * Utilidad para compartir un hilo usando el Intent nativo de Android.
 */
private fun compartirHilo(context: Context, titulo: String, autor: String, descripcion: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Master Martini Comunidad • Hilo de $autor:\n\"$titulo\"\n\n$descripcion\n\nDescubre más técnicas y recetas en la app oficial de Master Martini."
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartir hilo de Comunidad")
    context.startActivity(shareIntent)
}
