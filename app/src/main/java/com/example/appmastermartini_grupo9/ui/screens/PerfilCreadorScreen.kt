package com.example.appmastermartini_grupo9.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.model.RecetaOficial
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.FloatingBottomBar
import com.example.appmastermartini_grupo9.ui.theme.MartiniCharcoal
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel

/**
 * =========================================================================
 * PANTALLA DE PERFIL PÚBLICO DE CREADOR (DON SACO ROTO & CHEFS DE LA COMUNIDAD)
 * - Muestra la cabecera inmersiva Hero Portrait (estilo Olivia Beits / Pinterest)
 *   con foto a pantalla completa, degradado, identidad y estadísticas.
 * - Muestra abajo las recetas del creador en formato de Pines de Pinterest
 *   (mosaico escalonado de 2 columnas) para explorarlas y guardarlas fácilmente.
 * =========================================================================
 */
@Composable
fun PerfilCreadorScreen(
    autorNombre: String = "Don Saco Roto",
    mainViewModel: MainViewModel = viewModel(),
    onBack: () -> Unit = { mainViewModel.navigateBottomTab(Screen.Home) }
) {
    val context = LocalContext.current
    val todasLasRecetas by mainViewModel.recetasOficiales.collectAsState()
    val recetasGuardadasIds by mainViewModel.recetasGuardadasIds.collectAsState()
    val publicaciones by mainViewModel.publicacionesComunidad.collectAsState()

    var siguiendo by rememberSaveable { mutableStateOf(false) }
    var tabSeleccionada by rememberSaveable { mutableIntStateOf(0) } // 0 = Recetas, 1 = Comunidad

    // Intercepta el botón/gesto de atrás físico de Android
    BackHandler {
        onBack()
    }

    val recetasDelCreador = todasLasRecetas.filter {
        it.autor.equals(autorNombre, ignoreCase = true)
    }

    val publicacionesDelCreador = publicaciones.filter {
        it.autor.equals(autorNombre, ignoreCase = true)
    }

    val esDonSaco = autorNombre.contains("Don Saco Roto", ignoreCase = true)
    val fotoHeroRes = if (esDonSaco) R.drawable.avatar_don_saco_roto else R.drawable.receta_pasteleria
    val handleTexto = if (esDonSaco) "@donsacoroto" else "@${autorNombre.lowercase().replace(" ", "")}"
    val bioTexto = if (esDonSaco) {
        "Fanático de la buena pastelería y de las preparaciones generosas de Master Martini. ¡En mi taller nunca sobra nada, siempre hay espacio para otra porción!"
    } else {
        "Maestro pastelero y creador gastronómico apasionado de la comunidad Master Martini Chile."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. CABECERA HERO INMERSIVA ESTILO MOCKUP
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(490.dp)
            ) {
                // Imagen de fondo (Hero portrait)
                Image(
                    painter = painterResource(id = fotoHeroRes),
                    contentDescription = "Foto de portada $autorNombre",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Degradado inferior difuminado suave
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.Black.copy(alpha = 0.40f),
                                    0.18f to Color.Transparent,
                                    0.45f to Color.Black.copy(alpha = 0.28f),
                                    0.72f to Color(0xFF101413).copy(alpha = 0.88f),
                                    1.0f to MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                // Barra superior de navegación flotante translúcida (← y Compartir)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onBack,
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.45f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    Surface(
                        onClick = {
                            Toast.makeText(context, "Enlace al perfil de $autorNombre copiado", Toast.LENGTH_SHORT).show()
                        },
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.45f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir perfil",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }

                // Identidad central del Creador sobre el degradado
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                ) {
                    // Nombre del creador
                    Text(
                        text = autorNombre,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Color.White
                    )

                    // @handle
                    Text(
                        text = handleTexto,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.White.copy(alpha = 0.72f)
                    )

                    // Fila de botones de acción: Botón principal "Seguir" + botón de mensaje
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                    ) {
                        Button(
                            onClick = {
                                siguiendo = !siguiendo
                                val msg = if (siguiendo) "¡Ahora sigues a $autorNombre!" else "Dejaste de seguir a $autorNombre"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (siguiendo) MartiniGreenContainer else Color.White,
                                contentColor = if (siguiendo) MartiniGreenDark else Color.Black
                            ),
                            modifier = Modifier.height(44.dp)
                        ) {
                            if (siguiendo) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Siguiendo", fontWeight = FontWeight.Bold)
                            } else {
                                Text("Seguir", fontWeight = FontWeight.Bold)
                            }
                        }

                        Surface(
                            onClick = {
                                Toast.makeText(context, "Mensaje directo con $autorNombre disponible próximamente", Toast.LENGTH_SHORT).show()
                            },
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.22f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = "Enviar mensaje",
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }

                    // Estadísticas (Following | Followers | Creations / Recetas)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "89",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Siguiendo",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                                color = Color.White.copy(alpha = 0.65f)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "1.4K",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Seguidores",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                                color = Color.White.copy(alpha = 0.65f)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${recetasDelCreador.size}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Recetas",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                                color = Color.White.copy(alpha = 0.65f)
                            )
                        }
                    }

                    // Tarjeta translúcida de Bio
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.40f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = bioTexto,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.5.sp,
                                lineHeight = 17.sp
                            ),
                            color = Color.White.copy(alpha = 0.88f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            // 2. SECCIÓN INFERIOR: RECETAS EN FORMATO PINES DE PINTEREST & COMUNIDAD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .padding(top = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Selector de pestañas: Recetas · Comunidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        0 to ("Recetas" to "${recetasDelCreador.size}"),
                        1 to ("Comunidad" to "${publicacionesDelCreador.size}")
                    ).forEach { (idx, par) ->
                        val (label, count) = par
                        val seleccionada = tabSeleccionada == idx
                        Surface(
                            onClick = { tabSeleccionada = idx },
                            shape = RoundedCornerShape(50),
                            color = if (seleccionada) MartiniGreen else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$label ($count)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 13.5.sp
                                    ),
                                    color = if (seleccionada) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Contenido según pestaña
                if (tabSeleccionada == 0) {
                    // MOSAICO DE RECETAS TIPO PINES DE PINTEREST
                    if (recetasDelCreador.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(26.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Sin recetas publicadas aún",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Pronto $autorNombre compartirá nuevas creaciones.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                )
                            }
                        }
                    } else {
                        CreatorRecipePinsMasonry(
                            recetas = recetasDelCreador,
                            recetasGuardadasIds = recetasGuardadasIds,
                            onToggleGuardar = { mainViewModel.toggleGuardarReceta(it) },
                            onOpenReceta = { mainViewModel.navigateTo(Screen.RecetaDetalle(it)) }
                        )
                    }
                } else {
                    // PUBLICACIONES EN COMUNIDAD
                    if (publicacionesDelCreador.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(26.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Sin aportes en comunidad",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$autorNombre aún no ha participado en el foro.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                )
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            publicacionesDelCreador.forEach { pub ->
                                Surface(
                                    onClick = { mainViewModel.navigateTo(Screen.PostDetalle(pub.id)) },
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = MartiniGreenContainer.copy(alpha = 0.6f)
                                        ) {
                                            Text(
                                                text = pub.tipoPublicacion,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MartiniGreenDark,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = pub.titulo,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = pub.descripcion,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Barra inferior flotante fija
        FloatingBottomBar(
            selectedRoute = "",
            onNavigate = { screen -> mainViewModel.navigateBottomTab(screen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Mosaico escalonado de 2 columnas con tarjetas estilo Pines de Pinterest.
 */
@Composable
private fun CreatorRecipePinsMasonry(
    recetas: List<RecetaOficial>,
    recetasGuardadasIds: Set<Int>,
    onToggleGuardar: (Int) -> Unit,
    onOpenReceta: (Int) -> Unit
) {
    val izquierda = recetas.filterIndexed { index, _ -> index % 2 == 0 }
    val derecha = recetas.filterIndexed { index, _ -> index % 2 == 1 }
    val alturasIzq = listOf(240.dp, 270.dp, 220.dp)
    val alturasDer = listOf(265.dp, 230.dp, 255.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            izquierda.forEachIndexed { idx, receta ->
                CreatorPinCard(
                    receta = receta,
                    cardHeight = alturasIzq[idx % alturasIzq.size],
                    esGuardada = recetasGuardadasIds.contains(receta.id),
                    onToggleGuardar = { onToggleGuardar(receta.id) },
                    onClick = { onOpenReceta(receta.id) }
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            derecha.forEachIndexed { idx, receta ->
                CreatorPinCard(
                    receta = receta,
                    cardHeight = alturasDer[idx % alturasDer.size],
                    esGuardada = recetasGuardadasIds.contains(receta.id),
                    onToggleGuardar = { onToggleGuardar(receta.id) },
                    onClick = { onOpenReceta(receta.id) }
                )
            }
        }
    }
}

/**
 * Tarjeta individual de receta en formato Pin de Pinterest.
 */
@Composable
private fun CreatorPinCard(
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.50f to Color.Transparent,
                                1.0f to Color(0xFF141817).copy(alpha = 0.82f)
                            )
                        )
                    )
            )

            // Botón superior de Guardar/Marcador para que el usuario guarde la receta
            Surface(
                onClick = onToggleGuardar,
                shape = CircleShape,
                color = if (esGuardada) MartiniGreen else Color.White.copy(alpha = 0.90f),
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
                    .align(Alignment.TopEnd)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (esGuardada) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (esGuardada) "Quitar de guardados" else "Guardar receta",
                        tint = if (esGuardada) Color.White else MartiniCharcoal,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Info inferior: Título de la receta (sin tiempo)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(11.dp)
            ) {
                Text(
                    text = receta.titulo,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 17.sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
