package com.example.appmastermartini_grupo9.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.model.RecetaOficial
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.theme.MartiniCharcoal
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
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

    // Intercepta el botón/gesto de atrás físico de Android
    BackHandler {
        onBack()
    }

    val recetasDelCreador = todasLasRecetas.filter {
        it.autor.equals(autorNombre, ignoreCase = true)
    }


    val esDonSaco = autorNombre.contains("Don Saco Roto", ignoreCase = true)
    val fotoHeroRes = if (esDonSaco) R.drawable.avatar_don_saco_roto else R.drawable.receta_pasteleria
    val handleTexto = if (esDonSaco) "@donsacoroto" else "@${autorNombre.lowercase().replace(" ", "")}"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. CABECERA HERO INMERSIVA ESTILO MOCKUP
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(285.dp)
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
                }
            }

            // 2. SECCIÓN INFERIOR: RECETAS EN FORMATO PINES DE PINTEREST & COMUNIDAD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Recetas (${recetasDelCreador.size})", style = MaterialTheme.typography.titleLarge)
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
            }
        }

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
                    placeholder = painterResource(id = receta.imagenRes),
                    error = painterResource(id = receta.imagenRes),
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

            // Indicador de video si la receta tiene formato audiovisual
            if (receta.videoRes != 0 || receta.videoUri != null) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.55f),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp)
                        .align(Alignment.TopStart)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Video receta",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

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
