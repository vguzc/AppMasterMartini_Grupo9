package com.example.appmastermartini_grupo9.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.model.CursoGastronomico
import com.example.appmastermartini_grupo9.model.LeccionCurso
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.FloatingBottomBar
import com.example.appmastermartini_grupo9.ui.components.MartiniProfileAvatar
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.ui.theme.MartiniKhaki
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import kotlin.math.roundToInt

/**
 * =========================================================================
 * PANTALLA PRINCIPAL DE CURSOS: MASTER MARTINI ACADEMY
 * Vista inspirada en el mockup con Hero Banner de oferta, "My Course",
 * catálogo categorizado (Gratuitos vs De Pago) con fotos de portada reales
 * y detalle completo de lecciones.
 * =========================================================================
 */
@Composable
fun PantallaAnimaciones(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    avatarUri: Uri? = null
) {
    val cursos by mainViewModel.cursos.collectAsState()
    val progresoCursos by mainViewModel.progresoCursos.collectAsState()
    val cursosCompradosIds by mainViewModel.cursosCompradosIds.collectAsState()
    val suscritoPro by mainViewModel.suscritoAcademyPro.collectAsState()
    val cursoSeleccionado by mainViewModel.cursoSeleccionadoDetalle.collectAsState()

    var cursoParaComprar by remember { mutableStateOf<CursoGastronomico?>(null) }

    AnimatedContent(
        targetState = cursoSeleccionado,
        transitionSpec = {
            if (targetState != null) {
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width / 3 } + fadeOut()
            } else {
                slideInHorizontally { width -> -width / 3 } + fadeIn() togetherWith
                        slideOutHorizontally { width -> width } + fadeOut()
            }
        },
        label = "CursosTransicion"
    ) { cursoActual ->
        if (cursoActual != null) {
            BackHandler {
                mainViewModel.seleccionarCursoDetalle(null)
            }
            DetalleCursoScreen(
                curso = cursoActual,
                estaInscritoOComprado = cursoActual.esGratuito ||
                        cursosCompradosIds.contains(cursoActual.id) ||
                        suscritoPro,
                progreso = progresoCursos[cursoActual.id] ?: 0f,
                onBack = { mainViewModel.seleccionarCursoDetalle(null) },
                onInscribir = {
                    mainViewModel.inscribirCurso(cursoActual.id)
                },
                onComprar = {
                    cursoParaComprar = cursoActual
                },
                onToggleLeccion = { leccionId ->
                    mainViewModel.toggleLeccionCompletada(cursoActual.id, leccionId)
                }
            )
        } else {
            CatalogoCursosScreen(
                modifier = modifier,
                cursos = cursos,
                progresoCursos = progresoCursos,
                cursosCompradosIds = cursosCompradosIds,
                suscritoPro = suscritoPro,
                avatarUri = avatarUri,
                onAvatarClick = { mainViewModel.navigateTo(Screen.CamaraFoto) },
                onSeleccionarCurso = { curso ->
                    mainViewModel.seleccionarCursoDetalle(curso)
                },
                onComprarCurso = { curso ->
                    cursoParaComprar = curso
                },
                onNavigateBottom = { screen ->
                    mainViewModel.navigateBottomTab(screen)
                },
                onAbrirHero = {
                    val cursoHero = cursos.find { it.id == 1 } ?: cursos.first()
                    mainViewModel.seleccionarCursoDetalle(cursoHero)
                }
            )
        }
    }

    // Diálogo Modal para confirmar compra o suscripción Academy Pro
    if (cursoParaComprar != null) {
        val curso = cursoParaComprar!!
        DialogoCompraCurso(
            curso = curso,
            onDismiss = { cursoParaComprar = null },
            onConfirmarCompra = {
                mainViewModel.comprarCurso(curso.id)
                cursoParaComprar = null
            },
            onActivarMembresiaPro = {
                mainViewModel.toggleSuscripcionPro()
                mainViewModel.comprarCurso(curso.id)
                cursoParaComprar = null
            }
        )
    }
}

/**
 * =========================================================================
 * VISTA 1: CATÁLOGO DE CURSOS (MOCKUP PANTALLA IZQUIERDA)
 * Header con solo "Cursos", buscador y avatar a la derecha (sin notificaciones)
 * =========================================================================
 */
@Composable
private fun CatalogoCursosScreen(
    modifier: Modifier = Modifier,
    cursos: List<CursoGastronomico>,
    progresoCursos: Map<Int, Float>,
    cursosCompradosIds: Set<Int>,
    suscritoPro: Boolean,
    avatarUri: Uri? = null,
    onAvatarClick: () -> Unit,
    onSeleccionarCurso: (CursoGastronomico) -> Unit,
    onComprarCurso: (CursoGastronomico) -> Unit,
    onNavigateBottom: (Screen) -> Unit,
    onAbrirHero: () -> Unit
) {
    var filtroSeleccionado by rememberSaveable { mutableStateOf("Todos") }
    var busquedaQuery by rememberSaveable { mutableStateOf("") }
    var busquedaVisible by rememberSaveable { mutableStateOf(false) }

    val filtros = listOf("Todos", "Gratis", "Academy Pro", "Chocolatería", "Pastelería", "Panadería")

    // Curso actualmente en progreso (My Course)
    val cursoEnProgreso = remember(cursos, progresoCursos) {
        cursos.firstOrNull { (progresoCursos[it.id] ?: 0f) > 0f } ?: cursos.firstOrNull()
    }

    val cursosFiltrados = remember(cursos, filtroSeleccionado, busquedaQuery) {
        cursos.filter { c ->
            val coincideFiltro = when (filtroSeleccionado) {
                "Todos" -> true
                "Gratis" -> c.esGratuito
                "Academy Pro" -> !c.esGratuito
                "Chocolatería" -> c.insumoClave.contains("Ariba", ignoreCase = true) || c.titulo.contains("Chocolat", ignoreCase = true)
                "Pastelería" -> c.insumoClave.contains("Decor Up", ignoreCase = true) || c.insumoClave.contains("Mirall", ignoreCase = true)
                "Panadería" -> c.insumoClave.contains("Maxime", ignoreCase = true)
                else -> true
            }
            val coincideBusqueda = busquedaQuery.isBlank() ||
                    c.titulo.contains(busquedaQuery, ignoreCase = true) ||
                    c.chef.contains(busquedaQuery, ignoreCase = true) ||
                    c.insumoClave.contains(busquedaQuery, ignoreCase = true) ||
                    c.siglasBadge.contains(busquedaQuery, ignoreCase = true)

            coincideFiltro && coincideBusqueda
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 110.dp)
        ) {
            // 1. HEADER SUPERIOR SOLICITADO: Solo "Cursos" a la izquierda, búsqueda y avatar a la derecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cursos",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = {
                            busquedaVisible = !busquedaVisible
                            if (!busquedaVisible) busquedaQuery = ""
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = if (busquedaVisible) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Avatar unificado del usuario
                    MartiniProfileAvatar(
                        avatarUri = avatarUri,
                        size = 40.dp,
                        modifier = Modifier.clickable { onAvatarClick() }
                    )
                }
            }

            // Barra de búsqueda expandible
            AnimatedVisibility(visible = busquedaVisible) {
                OutlinedTextField(
                    value = busquedaQuery,
                    onValueChange = { busquedaQuery = it },
                    placeholder = { Text("Buscar por técnica, chef o insumo (ej: Ariba, Templado)...") },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MartiniGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 2. HERO BANNER ESTILO MOCKUP CON NUEVA FOTO REAL DE CHOCOLATERÍA
            HeroBannerCurso(
                modifier = Modifier.padding(horizontal = 20.dp),
                onExplorarClick = onAbrirHero
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 3. SECCIÓN "MY COURSE" / "EN PROGRESO" CON FOTO DE PORTADA
            if (cursoEnProgreso != null) {
                val progreso = progresoCursos[cursoEnProgreso.id] ?: 0.35f
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mi curso en progreso",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MartiniGreenContainer.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "En curso",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MartiniGreenDark
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    CardMyCourse(
                        curso = cursoEnProgreso,
                        progreso = progreso,
                        onClick = { onSeleccionarCurso(cursoEnProgreso) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 4. SECCIÓN "CATÁLOGO DE CURSOS" CON SELECTORES TIPO PÍLDORA (SIN PUNTO VERDE)
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Catálogo de cursos",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${cursosFiltrados.size} disponibles",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }

                // Filtros horizontales tipo píldora (Punto verde eliminado)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filtros.forEach { filtro ->
                        val isSelected = filtroSeleccionado == filtro
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) MartiniGreenDark else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable { filtroSeleccionado = filtro }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                if (filtro == "Academy Pro") {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = if (isSelected) MartiniKhaki else Color(0xFFFFB300),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = filtro,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Lista de Tarjetas de Cursos CON FOTOS DE PORTADA
                cursosFiltrados.forEach { curso ->
                    val yaCompradoOInscrito = curso.esGratuito ||
                            cursosCompradosIds.contains(curso.id) ||
                            suscritoPro
                    val progreso = progresoCursos[curso.id] ?: 0f

                    CursoCatalogCard(
                        curso = curso,
                        desbloqueado = yaCompradoOInscrito,
                        progreso = progreso,
                        onClick = { onSeleccionarCurso(curso) },
                        onComprar = { onComprarCurso(curso) }
                    )
                }
            }
        }

        // Floating Bottom Bar
        FloatingBottomBar(
            selectedRoute = Screen.Animaciones.route,
            onNavigate = { screen -> onNavigateBottom(screen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * =========================================================================
 * HERO BANNER SUPERIOR CON NUEVA FOTO REAL DE CHOCOLATERÍA (30% OFF)
 * =========================================================================
 */
@Composable
private fun HeroBannerCurso(
    modifier: Modifier = Modifier,
    onExplorarClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onExplorarClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(215.dp)
        ) {
            // FOTO DE ALGUNOS BOMBONES ABARCANDO TODO EL ESPACIO DEL BANNER
            Image(
                painter = painterResource(id = R.drawable.hero_banner_bombones),
                contentDescription = "Bombonería Ariba",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Degradado oscuro para asegurar contraste y legibilidad absoluta
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.88f),
                                Color.Black.copy(alpha = 0.65f),
                                Color.Black.copy(alpha = 0.30f)
                            )
                        )
                    )
            )

            // Contenido alineado sobre la foto de bombones
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFFFFD54F)
                        ) {
                            Text(
                                text = "30% OFF • MASTERCLASS DESTACADA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp,
                                    fontSize = 9.sp
                                ),
                                color = Color(0xFF3E2723),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        // Badge distintivo Ariba
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF5C3828).copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = "Ariba Puro",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Text(
                        text = "Bombonería Fina &\nTemplado con Ariba",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            lineHeight = 24.sp
                        )
                    )

                    Text(
                        text = "Atemperado en mármol, encamisados ultradelgados y ganaches de autor con cobertura pura.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.90f),
                            fontSize = 11.5.sp,
                            lineHeight = 15.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )
                }

                Button(
                    onClick = onExplorarClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1A1A1A)
                    ),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Explorar Curso",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                    )
                }
            }
        }
    }
}

/**
 * =========================================================================
 * TARJETA "MY COURSE" CON FOTO DE PORTADA (MOCKUP IZQUIERDA)
 * =========================================================================
 */
@Composable
private fun CardMyCourse(
    curso: CursoGastronomico,
    progreso: Float,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(curso.colorFondoCardHex)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Foto de portada en miniatura con sigla superpuesta
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = curso.imagenRes),
                        contentDescription = curso.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(curso.colorBadgeHex).copy(alpha = 0.92f)
                        ) {
                            Text(
                                text = curso.siglasBadge,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = curso.titulo,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = Color(0xFF1F1F1F),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${curso.duracion} restantes • ${curso.leccionesCount} lecciones",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF555555),
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Barra de progreso con porcentaje
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progreso.coerceIn(0.05f, 1f) },
                    color = Color(curso.colorBadgeHex),
                    trackColor = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier
                        .weight(1f)
                        .height(7.dp)
                        .clip(RoundedCornerShape(50))
                )
                Text(
                    text = "${(progreso * 100).roundToInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(curso.colorBadgeHex)
                    )
                )
            }
        }
    }
}

/**
 * =========================================================================
 * TARJETA DEL CATÁLOGO CON FOTO DE PORTADA REAL (MOCKUP IZQUIERDA)
 * Cada curso luce su fotografía gastronómica de portada con sigla y badges
 * =========================================================================
 */
@Composable
private fun CursoCatalogCard(
    curso: CursoGastronomico,
    desbloqueado: Boolean,
    progreso: Float,
    onClick: () -> Unit,
    onComprar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(curso.colorFondoCardHex)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // CABECERA CON FOTO DE PORTADA REAL DEL CURSO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
            ) {
                Image(
                    painter = painterResource(id = curso.imagenRes),
                    contentDescription = curso.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay degradado para asegurar contraste
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )

                // Sigla del insumo en squircle flotante arriba a la izquierda
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(curso.colorBadgeHex))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                        .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = curso.siglasBadge,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 19.sp,
                            color = Color.White
                        )
                    )
                }

                // Badges superiores: GRATIS vs PRECIO arriba a la derecha
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (curso.esGratuito) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFF00C853)
                        ) {
                            Text(
                                text = "GRATIS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontSize = 10.5.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFF212121).copy(alpha = 0.92f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD54F),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = curso.precioTexto,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        if (curso.precioOriginal != null) {
                            Text(
                                text = curso.precioOriginal,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    textDecoration = TextDecoration.LineThrough,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                // Nivel y Rating superpuestos en la parte inferior de la foto
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.Black.copy(alpha = 0.55f)
                    ) {
                        Text(
                            text = curso.nivel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "${curso.rating}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "(${curso.reviewsCount})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                        Text(text = "•", color = Color.White.copy(alpha = 0.7f))
                        Text(
                            text = curso.duracion,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // CUERPO INFERIOR CON DETALLES
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = curso.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A),
                        lineHeight = 20.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = curso.resumen,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF555555),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Barra de progreso si ya está empezado
                if (progreso > 0f) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { progreso },
                            color = Color(curso.colorBadgeHex),
                            trackColor = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier
                                .weight(1f)
                                .height(5.dp)
                                .clip(RoundedCornerShape(50))
                        )
                        Text(
                            text = "${(progreso * 100).roundToInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(curso.colorBadgeHex),
                                fontSize = 10.5.sp
                            )
                        )
                    }
                }

                // Fila de Chef y Botón de Acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${curso.chef} • ${curso.leccionesCount} clases",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF666666),
                            fontSize = 11.5.sp
                        ),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!curso.esGratuito && !desbloqueado) {
                        Button(
                            onClick = onComprar,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(curso.colorBadgeHex),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Inscribirme",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    } else {
                        Button(
                            onClick = onClick,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(curso.colorBadgeHex),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (progreso > 0f) "Continuar" else "Ver Curso",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * =========================================================================
 * VISTA 2: DETALLE DEL CURSO (MOCKUP PANTALLA DERECHA)
 * =========================================================================
 */
@Composable
private fun DetalleCursoScreen(
    curso: CursoGastronomico,
    estaInscritoOComprado: Boolean,
    progreso: Float,
    onBack: () -> Unit,
    onInscribir: () -> Unit,
    onComprar: () -> Unit,
    onToggleLeccion: (Int) -> Unit
) {
    val context = LocalContext.current
    var guardadoEnFavoritos by rememberSaveable { mutableStateOf(false) }
    var expandirDescripcion by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            // 1. TOP BAR DE DETALLE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Detalle del curso",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = { guardadoEnFavoritos = !guardadoEnFavoritos },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = if (guardadoEnFavoritos) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = if (guardadoEnFavoritos) MartiniGreenDark else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, "¡Mira este curso de ${curso.titulo} en Master Martini Academy!")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Compartir curso"))
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // 2. HERO PREVIEW CARD (MOCKUP DERECHA)
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(curso.colorBadgeHex)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = curso.imagenRes),
                        contentDescription = curso.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay oscuro degradado
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.25f),
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    // Sigla superior izquierda
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(curso.colorBadgeHex),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = curso.siglasBadge,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    // Badge de Gratuito / Precio arriba derecha
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (curso.esGratuito) Color(0xFF00C853) else Color(0xFFFFD54F),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = if (curso.esGratuito) "GRATUITO" else curso.precioTexto,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (curso.esGratuito) Color.White else Color(0xFF261914)
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    // Información inferior del Hero
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${curso.rating} (${curso.reviewsCount} reseñas)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(text = "•", color = Color.White.copy(alpha = 0.7f))
                            Text(
                                text = curso.duracion,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }

                        Text(
                            text = "Mentor: ${curso.chef}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. TÍTULO Y DESCRIPCIÓN "ABOUT THIS COURSE" (MOCKUP DERECHA)
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = curso.titulo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = (-0.3).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Subtítulo con insumo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MartiniGreenContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = curso.insumoClave,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MartiniGreenDark
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = "Nivel: ${curso.nivel}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // About this course
                Text(
                    text = "Acerca de este curso",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = if (expandirDescripcion) curso.descripcionDetallada else curso.resumen,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        lineHeight = 21.sp
                    )
                )

                Text(
                    text = if (expandirDescripcion) "Leer menos" else "Leer más",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MartiniGreen
                    ),
                    modifier = Modifier.clickable {
                        expandirDescripcion = !expandirDescripcion
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. LISTA DE LECCIONES ("LESSONS" - MOCKUP DERECHA)
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lecciones del curso",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${curso.lecciones.size} clases • ${curso.duracion}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }

                curso.lecciones.forEach { leccion ->
                    LeccionItemCard(
                        leccion = leccion,
                        cursoBadgeColor = Color(curso.colorBadgeHex),
                        desbloqueada = estaInscritoOComprado,
                        onToggle = { onToggleLeccion(leccion.id) }
                    )
                }
            }
        }

        // 5. BOTTOM ACTION BAR FIJA (MOCKUP DERECHA)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shadowElevation = 10.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                if (curso.esGratuito) {
                    if (!estaInscritoOComprado || progreso == 0f) {
                        Button(
                            onClick = onInscribir,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MartiniGreenDark,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Inscribirme Gratis al Curso",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                val siguiente = curso.lecciones.firstOrNull { !it.completada } ?: curso.lecciones.first()
                                onToggleLeccion(siguiente.id)
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MartiniGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = if (progreso >= 1f) "Curso Completado (Repasar)" else "Continuar con Siguiente Lección (${(progreso * 100).roundToInt()}%)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                } else {
                    // Curso de pago
                    if (!estaInscritoOComprado) {
                        Button(
                            onClick = onComprar,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(curso.colorBadgeHex),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD54F)
                                )
                                Text(
                                    text = "Comprar Curso • ${curso.precioTexto}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                val siguiente = curso.lecciones.firstOrNull { !it.completada } ?: curso.lecciones.first()
                                onToggleLeccion(siguiente.id)
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(curso.colorBadgeHex),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = if (progreso >= 1f) "Diplomado Completado" else "Continuar Lección (${(progreso * 100).roundToInt()}%)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * =========================================================================
 * ITEM DE LECCIÓN (MOCKUP DERECHA - LESSONS)
 * =========================================================================
 */
@Composable
private fun LeccionItemCard(
    leccion: LeccionCurso,
    cursoBadgeColor: Color,
    desbloqueada: Boolean,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(enabled = desbloqueada) { onToggle() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Squircle o círculo de Play / Check (estilo Mockup)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (!desbloqueada) Color.LightGray.copy(alpha = 0.5f)
                        else if (leccion.completada) Color(0xFF00C853)
                        else cursoBadgeColor.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!desbloqueada) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueada",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                } else if (leccion.completada) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completada",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Reproducir",
                        tint = cursoBadgeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "${leccion.numero.toString().padStart(2, '0')}. ${leccion.titulo}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = if (leccion.completada) FontWeight.Normal else FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = if (!desbloqueada) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = leccion.duracionTexto,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

/**
 * =========================================================================
 * DIÁLOGO MODAL INTERACTIVO DE COMPRA DE CURSO O ACADEMY PRO
 * =========================================================================
 */
@Composable
private fun DialogoCompraCurso(
    curso: CursoGastronomico,
    onDismiss: () -> Unit,
    onConfirmarCompra: () -> Unit,
    onActivarMembresiaPro: () -> Unit
) {
    var metodoPagoSeleccionado by remember { mutableStateOf("WebPay Plus") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Inscripción Academy",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = curso.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(curso.colorBadgeHex)
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total curso:")
                            Text(
                                text = curso.precioTexto,
                                fontWeight = FontWeight.Bold,
                                color = MartiniGreenDark
                            )
                        }
                        if (curso.precioOriginal != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Descuento aplicado:", color = Color(0xFFC62828))
                                Text(
                                    text = curso.descuentoBadge ?: "30% OFF",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC62828)
                                )
                            }
                        }
                        Text(
                            text = "Incluye acceso ilimitado a las ${curso.leccionesCount} lecciones y certificación digital oficial Master Martini.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                Text(
                    text = "Método de pago:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                listOf("WebPay Plus (Chile)", "Tarjeta de Crédito / Débito", "Membresía Academy Pro ($9.990/mes)").forEach { metodo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { metodoPagoSeleccionado = metodo }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (metodoPagoSeleccionado == metodo) MartiniGreen else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        ) {}
                        Text(
                            text = metodo,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (metodoPagoSeleccionado == metodo) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (metodoPagoSeleccionado.contains("Membresía")) {
                        onActivarMembresiaPro()
                    } else {
                        onConfirmarCompra()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MartiniGreenDark)
            ) {
                Text("Confirmar y Comenzar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
