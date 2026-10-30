package com.example.appmastermartini_grupo9.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.components.FloatingBottomBar
import com.example.appmastermartini_grupo9.ui.components.ImagenInteligente
import com.example.appmastermartini_grupo9.ui.components.MartiniProfileAvatar
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.ui.theme.MartiniCharcoal
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.ui.theme.MartiniKhaki
import com.example.appmastermartini_grupo9.viewmodel.EstadoViewModel
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import com.example.appmastermartini_grupo9.viewmodel.PerfilViewModel
import java.io.File
import kotlin.math.roundToInt

/**
 * Crea un archivo temporal seguro en cacheDir mediante FileProvider para captura con Cámara.
 */
fun crearUriTemporal(context: Context): Uri {
    val tempFile = File.createTempFile("perfil_temp_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}

private data class PinUsuarioItem(
    val uniqueKey: String,
    val titulo: String,
    val categoria: String,
    val tiempo: String,
    val esSubidaPorUsuario: Boolean,
    val imagenRes: Int,
    val imagenUri: Uri? = null,
    val onOpen: () -> Unit,
    val onRemoveOrUnbookmark: (() -> Unit)? = null
)

private const val CARPETA_TODAS_GUARDADAS_ID = -100

private data class MediaSlide(
    val resId: Int,
    val uri: Uri? = null
)

private data class CarpetaVistaUi(
    val id: Int,
    val nombre: String,
    val conteoRecetas: Int,
    val actualizadoTexto: String,
    val esPrivada: Boolean,
    val slides: List<MediaSlide>
)

/**
 * =========================================================================
 * SECCIÓN DE USUARIO MASTER MARTINI (IDENTIDAD PROPIA CON NESTED CAROUSEL)
 * - Cabecera unificada: Avatar MartiniProfileAvatar + Nombre / "Ver perfil"
 *   a la izquierda; botón "+" (idéntico al de las demás pantallas) +
 *   engranaje de Configuración (⚙️) a la derecha.
 * - 3 Pestañas claras: "Todos" · "Guardadas" · "Subidas".
 * - En "Guardadas": cuadrícula de carpetas con Nested Carousel / Swipeable
 *   Preview donde la primera carpeta fija es "Todas" (con todas las recetas
 *   guardadas), acompañada de las carpetas creadas por el usuario.
 * - Buscador con "Busca por nombre..." + botón CreateNewFolder en Guardadas.
 * =========================================================================
 */
@Composable
fun PerfilFotoScreen(
    viewModel: PerfilViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel(),
    estadoViewModel: EstadoViewModel = viewModel()
) {
    val imagenUri by viewModel.imagenUri.collectAsState()
    val modoOscuroActivo by estadoViewModel.activo.collectAsState()
    val mostrarMensajeGuardado by estadoViewModel.mostrarMensaje.collectAsState()

    val sesionActiva by mainViewModel.sesionActiva.collectAsState()
    val nombreUsuario by mainViewModel.nombreUsuario.collectAsState()
    val handleUsuario by mainViewModel.handleUsuario.collectAsState()
    val correoUsuario by mainViewModel.correoUsuario.collectAsState()
    val rolUsuario by mainViewModel.rolUsuario.collectAsState()

    val todasLasRecetas by mainViewModel.recetasOficiales.collectAsState()
    val recetasGuardadasIds by mainViewModel.recetasGuardadasIds.collectAsState()
    val carpetasUsuario by mainViewModel.carpetasUsuario.collectAsState()
    val cursos by mainViewModel.cursos.collectAsState()
    val progresoCursos by mainViewModel.progresoCursos.collectAsState()
    val publicaciones by mainViewModel.publicacionesComunidad.collectAsState()

    val context = LocalContext.current
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var permisoCamaraConcedido by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.actualizarImagen(it) }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && tempCameraUri != null) {
            viewModel.actualizarImagen(tempCameraUri)
        }
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoCamaraConcedido = concedido
        if (concedido) {
            val uri = crearUriTemporal(context)
            tempCameraUri = uri
            camaraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    // 0 = Todos, 1 = Guardadas (carpetas con carpeta "Todas"), 2 = Subidas, 3 = Comunidad
    var pestanaActiva by rememberSaveable { mutableIntStateOf(0) }
    var filtroCategoriaTodos by rememberSaveable { mutableStateOf("Todas") }
    var filtroCarpetasGuardadas by rememberSaveable { mutableStateOf("Todas") }
    var filtroCategoriaSubidas by rememberSaveable { mutableStateOf("Todas") }

    var busqueda by rememberSaveable { mutableStateOf("") }

    // Sub-vistas contextuales: abre por defecto el perfil con recetas (false) y el engranaje abre configuración (true)
    var mostrarConfiguracion by rememberSaveable { mutableStateOf(false) }
    var carpetaAbiertaId by rememberSaveable { mutableIntStateOf(-1) }
    var mostrarDialogoNuevaCarpeta by remember { mutableStateOf(false) }
    var mostrarDialogoEditarCarpeta by remember { mutableStateOf(false) }
    var mostrarDialogoEditarFotoPerfil by remember { mutableStateOf(false) }

    // Intercepta el botón/gesto de atrás nativo de Android
    BackHandler {
        if (carpetaAbiertaId != -1) {
            carpetaAbiertaId = -1
        } else if (mostrarConfiguracion) {
            // Si está en Configuración ("Tu cuenta"), volver al perfil
            mostrarConfiguracion = false
        } else {
            // Si está en el perfil, volver a Descubre
            mainViewModel.navigateBottomTab(Screen.Home)
        }
    }

    val misRecetasSubidas = todasLasRecetas.filter { it.esPropiaUsuario }
    val recetasGuardadas = todasLasRecetas.filter { recetasGuardadasIds.contains(it.id) }
    val todasRecetasUsuario = misRecetasSubidas + recetasGuardadas.filter { guardada ->
        misRecetasSubidas.none { it.id == guardada.id }
    }

    val pinesTodos = todasRecetasUsuario.map { rec ->
        PinUsuarioItem(
            uniqueKey = "all_${rec.id}",
            titulo = rec.titulo,
            categoria = rec.categoria,
            tiempo = rec.tiempo,
            esSubidaPorUsuario = rec.esPropiaUsuario,
            imagenRes = rec.imagenRes,
            imagenUri = rec.imagenUri,
            onOpen = { mainViewModel.navigateTo(Screen.RecetaDetalle(rec.id)) },
            onRemoveOrUnbookmark = {
                if (rec.esPropiaUsuario) {
                    mainViewModel.eliminarMiReceta(rec.id)
                } else {
                    mainViewModel.toggleGuardarReceta(rec.id)
                }
            }
        )
    }

    val pinesSubidos = misRecetasSubidas.map { rec ->
        PinUsuarioItem(
            uniqueKey = "own_${rec.id}",
            titulo = rec.titulo,
            categoria = rec.categoria,
            tiempo = rec.tiempo,
            esSubidaPorUsuario = true,
            imagenRes = rec.imagenRes,
            imagenUri = rec.imagenUri,
            onOpen = { mainViewModel.navigateTo(Screen.RecetaDetalle(rec.id)) },
            onRemoveOrUnbookmark = { mainViewModel.eliminarMiReceta(rec.id) }
        )
    }

    val pinesGuardados = recetasGuardadas.map { rec ->
        PinUsuarioItem(
            uniqueKey = "saved_${rec.id}",
            titulo = rec.titulo,
            categoria = rec.categoria,
            tiempo = rec.tiempo,
            esSubidaPorUsuario = false,
            imagenRes = rec.imagenRes,
            imagenUri = rec.imagenUri,
            onOpen = { mainViewModel.navigateTo(Screen.RecetaDetalle(rec.id)) },
            onRemoveOrUnbookmark = { mainViewModel.toggleGuardarReceta(rec.id) }
        )
    }

    // =========================================================================
    // DIÁLOGO: CREAR NUEVA COLECCIÓN CON NOMBRE ELEGIDO POR EL USUARIO
    // =========================================================================
    if (mostrarDialogoNuevaCarpeta) {
        var nombreNueva by remember { mutableStateOf("") }
        var esPrivadaNueva by remember { mutableStateOf(false) }
        val seleccionadasIds = remember { mutableStateListOf<Int>() }
        var errorNombre by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { mostrarDialogoNuevaCarpeta = false },
            title = {
                Text(
                    text = "Nueva carpeta",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nombreNueva,
                        onValueChange = {
                            nombreNueva = it
                            errorNombre = false
                        },
                        label = { Text("Nombre de la carpeta") },
                        placeholder = { Text("Ej: Tartas de verano, Postres Ariba...") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorNombre) {
                        Text(
                            text = "Escribe un nombre para tu carpeta.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    FilterChip(
                        selected = esPrivadaNueva,
                        onClick = { esPrivadaNueva = !esPrivadaNueva },
                        label = { Text("Carpeta privada") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MartiniGreenContainer,
                            selectedLabelColor = MartiniGreen
                        )
                    )

                    Text(
                        text = "Selecciona recetas para incluir:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    todasLasRecetas.forEach { rec ->
                        val marcada = seleccionadasIds.contains(rec.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (marcada) seleccionadasIds.remove(rec.id) else seleccionadasIds.add(rec.id)
                                }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (rec.imagenUri != null) {
                                AsyncImage(
                                    model = rec.imagenUri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = rec.imagenRes),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                            Text(
                                text = rec.titulo,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (marcada) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (marcada) MartiniGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nombreNueva.trim().isEmpty()) {
                            errorNombre = true
                        } else {
                            mainViewModel.crearCarpeta(
                                nombre = nombreNueva,
                                esPrivada = esPrivadaNueva,
                                esGrupo = false,
                                recetasInicialesIds = seleccionadasIds.toList()
                            )
                            pestanaActiva = 1
                            filtroCarpetasGuardadas = "Todas"
                            mostrarDialogoNuevaCarpeta = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Crear carpeta", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoNuevaCarpeta = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // =========================================================================
    // DIÁLOGO: CAMBIAR FOTO DE PERFIL (CÁMARA FILEPROVIDER / GALERÍA)
    // =========================================================================
    if (mostrarDialogoEditarFotoPerfil) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEditarFotoPerfil = false },
            title = {
                Text(
                    text = "Foto de perfil",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Actualiza tu fotografía de perfil pastelero:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val concedido = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                                permisoCamaraConcedido = concedido
                                if (concedido) {
                                    val uri = crearUriTemporal(context)
                                    tempCameraUri = uri
                                    camaraLauncher.launch(uri)
                                } else {
                                    permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                                }
                                mostrarDialogoEditarFotoPerfil = false
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(17.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cámara", color = Color.White)
                        }

                        Button(
                            onClick = {
                                galeriaLauncher.launch("image/*")
                                mostrarDialogoEditarFotoPerfil = false
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = MartiniDarkKhaki),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(17.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Galería", color = Color.White)
                        }
                    }

                    if (imagenUri != null) {
                        OutlinedButton(
                            onClick = {
                                viewModel.limpiarImagen()
                                mostrarDialogoEditarFotoPerfil = false
                                Toast.makeText(context, "Foto de perfil restablecida", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Quitar foto actual", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEditarFotoPerfil = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // =========================================================================
    // SUB-VISTA 1: PANTALLA DE CONFIGURACIÓN DE USUARIO ("TU CUENTA" - PINTEREST)
    // =========================================================================
    if (mostrarConfiguracion) {
        ConfiguracionUsuarioScreen(
            mainViewModel = mainViewModel,
            imagenUri = imagenUri,
            onVerPerfil = { mostrarConfiguracion = false },
            modoOscuroActivo = modoOscuroActivo == true,
            mostrarMensajeGuardado = mostrarMensajeGuardado,
            onToggleModoOscuro = { estadoViewModel.alternarEstado() },
            onIrContacto = {
                mostrarConfiguracion = false
                mainViewModel.navigateTo(Screen.Registro)
            },
            onBack = { mostrarConfiguracion = false }
        )
        return
    }

    // =========================================================================
    // SUB-VISTA 2: DETALLE INTERIOR DE UNA CARPETA ABIERTA ("Todas" O PERSONALIZADA)
    // =========================================================================
    if (carpetaAbiertaId != -1) {
        if (carpetaAbiertaId == CARPETA_TODAS_GUARDADAS_ID) {
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
                        .padding(PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 110.dp)),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            onClick = { carpetaAbiertaId = -1 },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver a Guardadas",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Todas",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = if (pinesGuardados.size == 1) "1 receta guardada" else "${pinesGuardados.size} recetas guardadas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
                            )
                        }
                    }

                    if (pinesGuardados.isEmpty()) {
                        EmptyPinterestBoardState(
                            titulo = "Aún no tienes recetas guardadas",
                            subtitulo = "Toca el ícono de marcador en Descubre para guardar tus recetas favoritas aquí.",
                            textoBoton = "Explorar Descubre",
                            onClick = {
                                carpetaAbiertaId = -1
                                mainViewModel.navigateBottomTab(Screen.Home)
                            }
                        )
                    } else {
                        PinterestUserPinsMasonry(pines = pinesGuardados)
                    }
                }

                FloatingBottomBar(
                    selectedRoute = "",
                    onNavigate = { screen -> mainViewModel.navigateBottomTab(screen) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            return
        }

        val carpetaActual = carpetasUsuario.find { it.id == carpetaAbiertaId }

        if (carpetaActual == null) {
            carpetaAbiertaId = -1
        } else {
            if (mostrarDialogoEditarCarpeta) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoEditarCarpeta = false },
                    title = {
                        Text(
                            text = "Organizar \"${carpetaActual.nombre}\"",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            todasLasRecetas.forEach { rec ->
                                val incluida = carpetaActual.recetasOficialesIds.contains(rec.id)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            mainViewModel.toggleRecetaEnCarpeta(carpetaActual.id, rec.id)
                                        }
                                        .padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (rec.imagenUri != null) {
                                        AsyncImage(
                                            model = rec.imagenUri,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(id = rec.imagenRes),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )
                                    }
                                    Text(
                                        text = rec.titulo,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 2,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = if (incluida) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (incluida) MartiniGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { mostrarDialogoEditarCarpeta = false },
                            colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Listo", color = Color.White)
                        }
                    }
                )
            }

            val ids = carpetaActual.recetasOficialesIds
            val pinesCarpeta = todasLasRecetas.filter { ids.contains(it.id) }.map { rec ->
                PinUsuarioItem(
                    uniqueKey = "folder_rec_${rec.id}",
                    titulo = rec.titulo,
                    categoria = rec.categoria,
                    tiempo = rec.tiempo,
                    esSubidaPorUsuario = rec.esPropiaUsuario,
                    imagenRes = rec.imagenRes,
                    imagenUri = rec.imagenUri,
                    onOpen = { mainViewModel.navigateTo(Screen.RecetaDetalle(rec.id)) },
                    onRemoveOrUnbookmark = {
                        mainViewModel.toggleRecetaEnCarpeta(carpetaActual.id, rec.id)
                    }
                )
            }

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
                        .padding(PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 110.dp)),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                onClick = { carpetaAbiertaId = -1 },
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver a colecciones",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = carpetaActual.nombre,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = if (pinesCarpeta.size == 1) "1 receta" else "${pinesCarpeta.size} recetas",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(onClick = { mostrarDialogoEditarCarpeta = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Añadir receta",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            IconButton(
                                onClick = {
                                    mainViewModel.eliminarCarpeta(carpetaActual.id)
                                    carpetaAbiertaId = -1
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Eliminar colección",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    if (pinesCarpeta.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Esta colección está vacía",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Button(
                                    onClick = { mostrarDialogoEditarCarpeta = true },
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                                ) {
                                    Text("Agregar recetas", color = Color.White)
                                }
                            }
                        }
                    } else {
                        PinterestUserPinsMasonry(pines = pinesCarpeta)
                    }
                }

                FloatingBottomBar(
                    selectedRoute = "",
                    onNavigate = { screen -> mainViewModel.navigateBottomTab(screen) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            return
        }
    }

    // =========================================================================
    // VISTA PRINCIPAL DE PERFIL DE USUARIO
    // =========================================================================
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
            // 1. HERO PORTRAIT HEADER (ESTILO MOCKUP OLIVIA BEITS)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(490.dp)
            ) {
                // Imagen de portada / fondo del hero
                if (imagenUri != null) {
                    AsyncImage(
                        model = imagenUri,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Portada limpia neutral cuando no hay foto personalizada
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF0F3529),
                                        Color(0xFF1B4D3E),
                                        Color(0xFF265848)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(96.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (sesionActiva && nombreUsuario.isNotBlank()) nombreUsuario.take(1).uppercase() else "M",
                                    color = Color.White,
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Degradado inferior y viñeta translúcida
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

                // Barra superior flotante translúcida: ← y ⚙️
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
                        onClick = { mainViewModel.navigateBottomTab(Screen.Home) },
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.45f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver a Descubre",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    Surface(
                        onClick = { mostrarConfiguracion = true },
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.45f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Configuración",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Información central sobre el degradado
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 14.dp)
                ) {
                    // Nombre en tipografía blanca y destacada
                    Text(
                        text = if (sesionActiva) nombreUsuario else "Invitado",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Color.White
                    )

                    // Handle @usuario
                    Text(
                        text = "@${if (sesionActiva) handleUsuario else "invitado"}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.White.copy(alpha = 0.72f)
                    )

                    // Fila de botones de acción
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                    ) {
                        if (!sesionActiva) {
                            Button(
                                onClick = { mainViewModel.navigateTo(Screen.Login) },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MartiniGreen,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text(
                                    text = "Iniciar Sesión / Registrarse",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        } else {
                            Button(
                                onClick = { mostrarDialogoEditarFotoPerfil = true },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text(
                                    text = "Cambiar foto",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp
                                    ),
                                    color = Color.Black
                                )
                            }

                            Surface(
                                onClick = { mostrarDialogoEditarFotoPerfil = true },
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.22f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Cambiar foto",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
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
                                text = "186",
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
                                text = "24K",
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
                                text = "${misRecetasSubidas.size}",
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
                            text = "Especialista en repostería fina y chocolatería artesanal con coberturas Ariba y cremas Decor Up de Master Martini Chile.",
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

            // 2. SECCIÃ“N DE RECETAS EN FORMATO PINES DE PINTEREST
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .padding(top = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Selector de pestaÃ±as: Guardadas pÃºblicas Â· Subidas Â· Todas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        0 to ("Guardadas" to "${recetasGuardadas.size}"),
                        1 to ("Subidas" to "${misRecetasSubidas.size}"),
                        2 to ("Todas" to "${todasRecetasUsuario.size}")
                    ).forEach { (idx, par) ->
                        val (label, count) = par
                        val seleccionada = pestanaActiva == idx
                        Surface(
                            onClick = { pestanaActiva = idx },
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
                                        fontSize = 13.sp
                                    ),
                                    color = if (seleccionada) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Buscador sutil
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(50))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.size(19.dp)
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            if (busqueda.isEmpty()) {
                                Text(
                                    text = "Buscar en mis recetas...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            BasicTextField(
                                value = busqueda,
                                onValueChange = { busqueda = it },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                cursorBrush = SolidColor(MartiniGreen),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        if (busqueda.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar bÃºsqueda",
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { busqueda = "" }
                            )
                        }
                    }
                }

                // CuadrÃ­cula estilo Pines de Pinterest con las fotos de las recetas
                when (pestanaActiva) {
                    0 -> {
                        val guardadasFiltradas = pinesGuardados.filter { pin ->
                            busqueda.isBlank() || pin.titulo.contains(busqueda, ignoreCase = true) || pin.categoria.contains(busqueda, ignoreCase = true)
                        }

                        if (guardadasFiltradas.isEmpty()) {
                            EmptyPinterestBoardState(
                                titulo = "Aún no tienes recetas guardadas",
                                subtitulo = "Toca el ícono de marcador en cualquier receta de Descubre para guardarla y verla públicamente aquí.",
                                textoBoton = "Explorar Descubre",
                                onClick = { mainViewModel.navigateBottomTab(Screen.Home) }
                            )
                        } else {
                            PinterestUserPinsMasonry(pines = guardadasFiltradas)
                        }
                    }

                    1 -> {
                        val subidasFiltradas = pinesSubidos.filter { pin ->
                            busqueda.isBlank() || pin.titulo.contains(busqueda, ignoreCase = true) || pin.categoria.contains(busqueda, ignoreCase = true)
                        }

                        if (subidasFiltradas.isEmpty()) {
                            EmptyPinterestBoardState(
                                titulo = "Aún no has subido recetas",
                                subtitulo = "Comparte tus preparaciones con la comunidad tocando el botón + arriba a la derecha.",
                                textoBoton = "Subir receta",
                                onClick = { mainViewModel.navigateTo(Screen.CrearReceta) }
                            )
                        } else {
                            PinterestUserPinsMasonry(pines = subidasFiltradas)
                        }
                    }

                    else -> {
                        val todasFiltradas = pinesTodos.filter { pin ->
                            busqueda.isBlank() || pin.titulo.contains(busqueda, ignoreCase = true) || pin.categoria.contains(busqueda, ignoreCase = true)
                        }

                        if (todasFiltradas.isEmpty()) {
                            EmptyPinterestBoardState(
                                titulo = "No hay recetas disponibles",
                                subtitulo = "Tus recetas guardadas y subidas aparecerÃ¡n reunidas en esta vista.",
                                textoBoton = "Explorar Descubre",
                                onClick = { mainViewModel.navigateBottomTab(Screen.Home) }
                            )
                        } else {
                            PinterestUserPinsMasonry(pines = todasFiltradas)
                        }
                    }
                }
            }
        }
        FloatingBottomBar(
            selectedRoute = Screen.CamaraFoto.route,
            onNavigate = { screen -> mainViewModel.navigateBottomTab(screen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * =========================================================================
 * TARJETA DE COLECCIÓN CON NESTED CAROUSEL / SWIPEABLE PREVIEW
 * Permite deslizar horizontalmente las fotos de las recetas dentro de cada
 * colección sin abrirla, mostrando indicadores animados y contador sutil.
 * =========================================================================
 */
@Composable
private fun CollectionSwipeableCard(
    nombre: String,
    conteoRecetas: Int,
    actualizadoTexto: String,
    esPrivada: Boolean,
    slides: List<MediaSlide>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val placeholderColor = MaterialTheme.colorScheme.surfaceVariant
    val pageCount = slides.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(placeholderColor)
        ) {
            if (slides.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onClick)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val slide = slides[page]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = onClick)
                    ) {
                        if (slide.uri != null) {
                            AsyncImage(
                                model = slide.uri,
                                contentDescription = nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = slide.resId),
                                contentDescription = nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                // Sombra inferior sutil para que los puntitos del carrusel se lean con nitidez
                if (slides.size > 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.52f)
                                    )
                                )
                            )
                    )

                    // Puntitos indicadores del Nested Carousel
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 9.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(slides.size.coerceAtMost(6)) { index ->
                            val activo = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .height(5.dp)
                                    .width(if (activo) 14.dp else 5.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        if (activo) Color.White else Color.White.copy(alpha = 0.48f)
                                    )
                            )
                        }
                    }

                    // Contador sutil arriba a la derecha (ej: 1/3)
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.Black.copy(alpha = 0.52f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1}/${slides.size}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                letterSpacing = 0.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (esPrivada) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.58f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Colección privada",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 2.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (conteoRecetas == 1) "1 Receta" else "$conteoRecetas Recetas",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
                )
                Text(
                    text = actualizadoTexto,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.48f)
                )
            }
        }
    }
}

/**
 * Mosaico escalonado de 2 columnas para recetas guardadas o subidas.
 */
@Composable
private fun PinterestUserPinsMasonry(pines: List<PinUsuarioItem>) {
    val izquierda = pines.filterIndexed { index, _ -> index % 2 == 0 }
    val derecha = pines.filterIndexed { index, _ -> index % 2 == 1 }
    val alturasIzq = listOf(235.dp, 265.dp, 220.dp)
    val alturasDer = listOf(265.dp, 225.dp, 250.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            izquierda.forEachIndexed { idx, pin ->
                UserPinCard(
                    pin = pin,
                    cardHeight = alturasIzq[idx % alturasIzq.size]
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            derecha.forEachIndexed { idx, pin ->
                UserPinCard(
                    pin = pin,
                    cardHeight = alturasDer[idx % alturasDer.size]
                )
            }
        }
    }
}

@Composable
private fun UserPinCard(
    pin: PinUsuarioItem,
    cardHeight: Dp
) {
    Surface(
        onClick = pin.onOpen,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (pin.imagenUri != null) {
                AsyncImage(
                    model = pin.imagenUri,
                    contentDescription = pin.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = pin.imagenRes),
                    contentDescription = pin.titulo,
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
                                1.0f to Color(0xFF141817).copy(alpha = 0.80f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pin.esSubidaPorUsuario) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MartiniGreen.copy(alpha = 0.92f)
                    ) {
                        Text(
                            text = "Subida",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                letterSpacing = 0.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(1.dp))
                }

                if (pin.onRemoveOrUnbookmark != null) {
                    Surface(
                        onClick = pin.onRemoveOrUnbookmark,
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.90f),
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (pin.esSubidaPorUsuario) Icons.Default.DeleteOutline else Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = if (pin.esSubidaPorUsuario) MaterialTheme.colorScheme.error else MartiniGreen,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(11.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.94f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MartiniCharcoal,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = pin.tiempo,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                letterSpacing = 0.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MartiniCharcoal
                        )
                    }
                }
                Text(
                    text = pin.titulo,
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

@Composable
private fun EmptyPinterestBoardState(
    titulo: String,
    subtitulo: String,
    textoBoton: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
            )
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
            ) {
                Text(textoBoton, color = Color.White)
            }
        }
    }
}

/**
 * =========================================================================
 * PANTALLA DE CONFIGURACIÓN Y PERFIL (PERTINENTE A MASTER MARTINI CHILE)
 * Organizada en 6 bloques claros adaptados al contexto de pastelería,
 * recetas, cursos Academy y foro de comunidad:
 * 1. Perfil y Cuenta (Foto Cámara/Galería, Datos personales, Seguridad, Plan Academy)
 * 2. Preferencias de la Aplicación (Tema DataStore, Unidades g/ml/°C, Idioma, Háptica)
 * 3. Notificaciones y Avisos (Push, Foro Comunidad, Recetas/Cursos, Boletín)
 * 4. Privacidad y Datos (Permisos con enlace a Ajustes Android, Visibilidad, Datos)
 * 5. Almacenamiento y Rendimiento (Limpieza real de caché y fotos temporales, HD Wi-Fi)
 * 6. Soporte y Ayuda (FAQ técnicas, Contacto Master Martini Chile, Legal y Versión)
 * =========================================================================
 */
/**
 * =========================================================================
 * SUB-VISTA: PANTALLA DE CONFIGURACIÓN "TU CUENTA" (ESTILO PINTEREST)
 * Replicando exactamente la estructura de la captura enviada por el usuario:
 * - Cabecera: <        Tu cuenta
 * - Tarjeta superior: Avatar circular, Nombre ("Valentina Soto"), @handle
 *   y botón "Ver perfil" (despliega la vista pública de Valentina, idéntica
 *   al formato de Don Saco Roto).
 * - Secciones de lista tipo Pinterest con chevron >:
 *   * Configuración: Gestión de la cuenta, Visibilidad del perfil,
 *     Notificaciones, Privacidad y datos, Almacenamiento y rendimiento.
 *   * Inicio de sesión y seguridad: Seguridad, Apariencia (Modo Claro/Oscuro
 *     con Switch directo y persistencia DataStore), Unidades e idioma.
 *   * Soporte y ayuda: Centro de ayuda (FAQ), Contacto con soporte técnico,
 *     Información legal y versión, Cerrar sesión, Eliminar cuenta.
 * - Barra inferior flotante FloatingBottomBar con pestaña Perfil activa.
 * =========================================================================
 */
@Composable
private fun ConfiguracionUsuarioScreen(
    mainViewModel: MainViewModel,
    imagenUri: Uri?,
    onVerPerfil: () -> Unit,
    modoOscuroActivo: Boolean,
    mostrarMensajeGuardado: Boolean,
    onToggleModoOscuro: () -> Unit,
    onIrContacto: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val sesionActiva by mainViewModel.sesionActiva.collectAsState()
    val nombreUsuario by mainViewModel.nombreUsuario.collectAsState()
    val handleUsuario by mainViewModel.handleUsuario.collectAsState()
    val correoUsuario by mainViewModel.correoUsuario.collectAsState()
    val telefonoUsuario by mainViewModel.telefonoUsuario.collectAsState()
    val rolUsuario by mainViewModel.rolUsuario.collectAsState()

    val notifPushGeneral by mainViewModel.notifPushGeneral.collectAsState()
    val notifForoComunidad by mainViewModel.notifForoComunidad.collectAsState()
    val notifRecetasYCursos by mainViewModel.notifRecetasYCursos.collectAsState()
    val notifCatalogoProductos by mainViewModel.notifCatalogoProductos.collectAsState()
    val boletinCorreo by mainViewModel.boletinCorreo.collectAsState()

    val perfilPublicoComunidad by mainViewModel.perfilPublicoComunidad.collectAsState()
    val altaCalidadSoloWifi by mainViewModel.altaCalidadSoloWifi.collectAsState()

    val todasLasRecetas by mainViewModel.recetasOficiales.collectAsState()
    val recetasGuardadasIds by mainViewModel.recetasGuardadasIds.collectAsState()
    val carpetasUsuario by mainViewModel.carpetasUsuario.collectAsState()

    var editNombre by remember(nombreUsuario) { mutableStateOf(nombreUsuario) }
    var editCorreo by remember(correoUsuario) { mutableStateOf(correoUsuario) }
    var editTelefono by remember(telefonoUsuario) { mutableStateOf(telefonoUsuario) }
    var editRol by remember(rolUsuario) { mutableStateOf(rolUsuario) }

    var tamanoCacheBytes by remember { mutableStateOf(calcularBytesDirectorio(context.cacheDir)) }

    // Diálogos modales Pinterest
    var mostrarDialogoGestionCuenta by remember { mutableStateOf(false) }
    var mostrarDialogoVisibilidad by remember { mutableStateOf(false) }
    var mostrarDialogoNotificaciones by remember { mutableStateOf(false) }
    var mostrarDialogoPrivacidad by remember { mutableStateOf(false) }
    var mostrarDialogoAlmacenamiento by remember { mutableStateOf(false) }
    var mostrarDialogoSeguridad by remember { mutableStateOf(false) }
    var mostrarDialogoFaq by remember { mutableStateOf(false) }
    var mostrarDialogoLegal by remember { mutableStateOf(false) }
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }
    var mostrarDialogoEliminarCuenta by remember { mutableStateOf(false) }
    var mostrarDialogoPassword by remember { mutableStateOf(false) }

    // Diálogo: Gestión de la cuenta (Datos personales y suscripción)
    if (mostrarDialogoGestionCuenta) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoGestionCuenta = false },
            title = {
                Text(
                    text = "Gestión de la cuenta",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Datos personales",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = editNombre,
                        onValueChange = { editNombre = it },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editCorreo,
                        onValueChange = { editCorreo = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editTelefono,
                        onValueChange = { editTelefono = it },
                        label = { Text("Teléfono de contacto") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editRol,
                        onValueChange = { editRol = it },
                        label = { Text("Especialidad / Ciudad") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        mainViewModel.actualizarDatosCuenta(
                            nombre = editNombre,
                            correo = editCorreo,
                            telefono = editTelefono,
                            especialidad = editRol
                        )
                        mostrarDialogoGestionCuenta = false
                        Toast.makeText(context, "Datos de cuenta actualizados", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Guardar cambios", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoGestionCuenta = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo: Visibilidad del perfil
    if (mostrarDialogoVisibilidad) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoVisibilidad = false },
            title = {
                Text(
                    text = "Visibilidad del perfil",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Configura quién puede ver tu actividad en la comunidad de Master Martini.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    SettingsSwitchRow(
                        titulo = "Perfil público en Comunidad",
                        subtitulo = "Muestra tus recetas subidas y aportes a otros usuarios de la app.",
                        checked = perfilPublicoComunidad,
                        onCheckedChange = { mainViewModel.togglePerfilPublicoComunidad() }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoVisibilidad = false },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Listo", color = Color.White)
                }
            }
        )
    }

    // Diálogo: Notificaciones
    if (mostrarDialogoNotificaciones) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoNotificaciones = false },
            title = {
                Text(
                    text = "Notificaciones y avisos",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SettingsSwitchRow(
                        titulo = "Notificaciones Push",
                        subtitulo = "Alertas generales en el dispositivo",
                        checked = notifPushGeneral,
                        onCheckedChange = { mainViewModel.toggleNotifPushGeneral() }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    SettingsSwitchRow(
                        titulo = "Foro Comunidad",
                        subtitulo = "Avisar cuando respondan a tus consultas técnicas",
                        checked = notifPushGeneral && notifForoComunidad,
                        enabled = notifPushGeneral,
                        onCheckedChange = { mainViewModel.toggleNotifForoComunidad() }
                    )
                    SettingsSwitchRow(
                        titulo = "Recetas y Cursos",
                        subtitulo = "Nuevas fórmulas oficiales ✓ Martini y clases",
                        checked = notifPushGeneral && notifRecetasYCursos,
                        enabled = notifPushGeneral,
                        onCheckedChange = { mainViewModel.toggleNotifRecetasYCursos() }
                    )
                    SettingsSwitchRow(
                        titulo = "Catálogo profesional",
                        subtitulo = "Lanzamientos de Ariba, Caravella y Decor Up",
                        checked = notifPushGeneral && notifCatalogoProductos,
                        enabled = notifPushGeneral,
                        onCheckedChange = { mainViewModel.toggleNotifCatalogoProductos() }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    SettingsSwitchRow(
                        titulo = "Boletín por correo",
                        subtitulo = "Resumen técnico mensual y recetarios descargables",
                        checked = boletinCorreo,
                        onCheckedChange = { mainViewModel.toggleBoletinCorreo() }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoNotificaciones = false },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Cerrar", color = Color.White)
                }
            }
        )
    }

    // Diálogo: Privacidad y datos
    if (mostrarDialogoPrivacidad) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoPrivacidad = false },
            title = {
                Text(
                    text = "Privacidad y datos",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Controla los permisos otorgados y la gestión de tu información personal.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    SettingsActionRow(
                        icon = Icons.Default.CameraAlt,
                        iconTint = MartiniGreen,
                        titulo = "Permisos del dispositivo",
                        subtitulo = "Cámara y almacenamiento en ajustes del sistema Android",
                        onClick = {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                            context.startActivity(intent)
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    SettingsActionRow(
                        icon = Icons.Default.Download,
                        iconTint = MartiniGreen,
                        titulo = "Descargar copia de mis datos",
                        subtitulo = "Exportar resumen de recetas subidas, colecciones y notas",
                        onClick = {
                            val propias = todasLasRecetas.count { it.esPropiaUsuario }
                            val guardadas = recetasGuardadasIds.size
                            val carpetas = carpetasUsuario.size
                            Toast.makeText(
                                context,
                                "Copia de datos generada: $propias recetas subidas, $guardadas guardadas y $carpetas carpetas",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoPrivacidad = false },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Listo", color = Color.White)
                }
            }
        )
    }

    // Diálogo: Almacenamiento y rendimiento
    if (mostrarDialogoAlmacenamiento) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAlmacenamiento = false },
            title = {
                Text(
                    text = "Almacenamiento y caché",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Archivos temporales y fotos",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Espacio ocupado: ${formatearTamanoCache(tamanoCacheBytes)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                        Button(
                            onClick = {
                                limpiarArchivosTemporales(context.cacheDir)
                                tamanoCacheBytes = calcularBytesDirectorio(context.cacheDir)
                                Toast.makeText(context, "Caché liberada correctamente", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                        ) {
                            Text("Limpiar", color = Color.White)
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    SettingsSwitchRow(
                        titulo = "Videos en HD solo con Wi-Fi",
                        subtitulo = "Ahorra datos móviles al ver clases de Martini Academy",
                        checked = altaCalidadSoloWifi,
                        onCheckedChange = { mainViewModel.toggleAltaCalidadSoloWifi() }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoAlmacenamiento = false },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Listo", color = Color.White)
                }
            }
        )
    }

    // Diálogo: Seguridad
    if (mostrarDialogoSeguridad) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoSeguridad = false },
            title = {
                Text(
                    text = "Seguridad e inicio de sesión",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingsActionRow(
                        icon = Icons.Default.Lock,
                        iconTint = MartiniGreen,
                        titulo = "Cambiar contraseña",
                        subtitulo = "Actualiza tu contraseña de acceso",
                        onClick = {
                            mostrarDialogoSeguridad = false
                            mostrarDialogoPassword = true
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    SettingsActionRow(
                        icon = Icons.Default.Security,
                        iconTint = MartiniGreen,
                        titulo = "Sesiones activas",
                        subtitulo = "Este dispositivo Android • Santiago, CL",
                        badgeText = if (sesionActiva) "Activa" else "Invitado",
                        onClick = {
                            Toast.makeText(context, "1 dispositivo conectado actualmente", Toast.LENGTH_SHORT).show()
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Autenticación en dos pasos (2FA)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Código SMS al iniciar sesión",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MartiniGreenContainer.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = "Activado",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MartiniGreenDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoSeguridad = false },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Listo", color = Color.White)
                }
            }
        )
    }

    // Diálogo: Cambiar contraseña
    if (mostrarDialogoPassword) {
        var passActual by remember { mutableStateOf("") }
        var passNueva by remember { mutableStateOf("") }
        var passConfirmar by remember { mutableStateOf("") }
        var errorPass by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { mostrarDialogoPassword = false },
            title = {
                Text(
                    text = "Cambiar contraseña",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = passActual,
                        onValueChange = {
                            passActual = it
                            errorPass = null
                        },
                        label = { Text("Contraseña actual") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = passNueva,
                        onValueChange = {
                            passNueva = it
                            errorPass = null
                        },
                        label = { Text("Nueva contraseña (mín. 6 caracteres)") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = passConfirmar,
                        onValueChange = {
                            passConfirmar = it
                            errorPass = null
                        },
                        label = { Text("Confirmar nueva contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorPass != null) {
                        Text(
                            text = errorPass!!,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when {
                            passNueva.length < 6 -> errorPass = "La nueva contraseña debe tener al menos 6 caracteres."
                            passNueva != passConfirmar -> errorPass = "Las contraseñas no coinciden."
                            else -> {
                                mostrarDialogoPassword = false
                                Toast.makeText(context, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Actualizar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoPassword = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo: Cerrar sesión
    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrarSesion = false },
            title = {
                Text(
                    text = "¿Cerrar sesión?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "¿Deseas cerrar tu sesión activa de ${if (sesionActiva) nombreUsuario else "pastelero"} en este dispositivo?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mainViewModel.cerrarSesion()
                        mostrarDialogoCerrarSesion = false
                        onBack()
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        mainViewModel.navigateTo(Screen.Login)
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Cerrar sesión", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCerrarSesion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo: Eliminar cuenta
    if (mostrarDialogoEliminarCuenta) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminarCuenta = false },
            title = {
                Text(
                    text = "¿Eliminar tu cuenta?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Se cerrará tu sesión activa y se restablecerán tus datos personales de perfil en este dispositivo.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mainViewModel.eliminarCuentaUsuario()
                        mostrarDialogoEliminarCuenta = false
                        Toast.makeText(context, "Cuenta eliminada y sesión cerrada", Toast.LENGTH_SHORT).show()
                        mainViewModel.navigateTo(Screen.Login)
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar cuenta", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminarCuenta = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo: FAQ
    if (mostrarDialogoFaq) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoFaq = false },
            title = {
                Text(
                    text = "Preguntas frecuentes",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FaqItem(
                        pregunta = "¿Qué significa el sello ✓ de palomita blanca en Descubre?",
                        respuesta = "Indica que la receta fue formulada y verificada por el equipo técnico de Master Martini Chile con proporciones exactas de línea profesional."
                    )
                    FaqItem(
                        pregunta = "¿Cómo organizo mis recetas guardadas?",
                        respuesta = "En tu perfil entra a la pestaña Guardadas: la carpeta 'Todas' agrupa todo lo guardado y con el ícono de carpeta puedes crear colecciones propias."
                    )
                    FaqItem(
                        pregunta = "¿Dónde hago consultas técnicas sobre Decor Up, Ariba o Caravella?",
                        respuesta = "Toca el botón '+' arriba a la derecha y elige 'Hacer consulta o post' para publicar tu duda o resultado en el Foro de Comunidad."
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoFaq = false },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Entendido", color = Color.White)
                }
            }
        )
    }

    // Diálogo: Información legal
    if (mostrarDialogoLegal) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoLegal = false },
            title = {
                Text(
                    text = "Información legal y versión",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "App Master Martini Chile • Grupo Unigrà",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "• Términos de uso: Las fichas técnicas y formulaciones oficiales pertenecen a Master Martini / Unigrà.\n• Privacidad: Las fotografías capturadas con Cámara o Galería se procesan localmente mediante FileProvider seguro.\n• Licencias Open Source: Jetpack Compose Material 3, AndroidX Navigation, DataStore Preferences y Coil.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Versión actual: 2.4.0 (Build Grupo 9)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MartiniGreen
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarDialogoLegal = false },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Cerrar", color = Color.White)
                }
            }
        )
    }

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
                .padding(PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 120.dp)),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Cabecera superior idéntica a Pinterest: <        Tu cuenta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al perfil",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "Tu cuenta",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.size(40.dp))
            }

            // 2. Tarjeta superior del perfil (Formato Pinterest)
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MartiniProfileAvatar(
                            avatarUri = imagenUri,
                            size = 64.dp
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = if (sesionActiva) nombreUsuario else "Invitado",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "@${if (sesionActiva) handleUsuario else "invitado"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                    }

                    // Botón "Ver perfil" (Único botón, sin opciones inexistentes como compartir perfil)
                    Button(
                        onClick = onVerPerfil,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(
                            text = "Ver perfil",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp
                            )
                        )
                    }
                }
            }

            // =================================================================
            // SECCIÓN 1: CONFIGURACIÓN
            // =================================================================
            PinterestSectionHeader("Configuración")

            PinterestSettingsItem(
                titulo = "Gestión de la cuenta",
                subtitulo = "Nombre, correo, teléfono y suscripción",
                onClick = { mostrarDialogoGestionCuenta = true }
            )

            PinterestSettingsItem(
                titulo = "Visibilidad del perfil",
                subtitulo = "Perfil público en Comunidad",
                onClick = { mostrarDialogoVisibilidad = true }
            )

            PinterestSettingsItem(
                titulo = "Notificaciones",
                subtitulo = "Alertas push, foro, recetas y boletín",
                onClick = { mostrarDialogoNotificaciones = true }
            )

            PinterestSettingsItem(
                titulo = "Privacidad y datos",
                subtitulo = "Permisos de cámara y copia de datos",
                onClick = { mostrarDialogoPrivacidad = true }
            )

            PinterestSettingsItem(
                titulo = "Almacenamiento y rendimiento",
                subtitulo = "Caché temporal y ahorro de datos móviles",
                onClick = { mostrarDialogoAlmacenamiento = true }
            )

            // =================================================================
            // SECCIÓN 2: INICIO DE SESIÓN Y SEGURIDAD
            // =================================================================
            PinterestSectionHeader("Inicio de sesión y seguridad")

            PinterestSettingsItem(
                titulo = "Seguridad",
                subtitulo = "Cambio de contraseña y sesiones activas",
                onClick = { mostrarDialogoSeguridad = true }
            )

            PinterestSettingsItem(
                titulo = "Apariencia (Tema)",
                subtitulo = if (modoOscuroActivo) "Modo oscuro activo" else "Modo claro activo",
                trailingContent = {
                    Switch(
                        checked = modoOscuroActivo,
                        onCheckedChange = { onToggleModoOscuro() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MartiniKhaki,
                            checkedTrackColor = MartiniGreen
                        )
                    )
                },
                onClick = { onToggleModoOscuro() }
            )

            AnimatedVisibility(visible = mostrarMensajeGuardado) {
                Text(
                    text = "✓ Preferencia de tema guardada en DataStore",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (modoOscuroActivo) MartiniKhaki else MartiniGreen,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

            // =================================================================
            // SECCIÓN 3: SOPORTE Y AYUDA
            // =================================================================
            PinterestSectionHeader("Soporte y ayuda")

            PinterestSettingsItem(
                titulo = "Centro de ayuda (FAQ)",
                subtitulo = "Preguntas técnicas y formulaciones",
                onClick = { mostrarDialogoFaq = true }
            )

            PinterestSettingsItem(
                titulo = "Contacto con soporte técnico",
                subtitulo = "Atención Master Martini Chile",
                onClick = onIrContacto
            )

            PinterestSettingsItem(
                titulo = "Información legal y versión",
                subtitulo = "Términos, licencias • v2.4.0",
                onClick = { mostrarDialogoLegal = true }
            )

            PinterestSettingsItem(
                titulo = "Cambiar de cuenta / Login",
                subtitulo = "Iniciar sesión con otra cuenta o crear cuenta",
                onClick = { mainViewModel.navigateTo(Screen.Login) }
            )

            PinterestSettingsItem(
                titulo = "Cerrar sesión",
                subtitulo = "Finalizar sesión en este dispositivo",
                onClick = { mostrarDialogoCerrarSesion = true }
            )

            PinterestSettingsItem(
                titulo = "Eliminar cuenta",
                subtitulo = "Restablecer datos locales de la cuenta",
                isDestructive = true,
                onClick = { mostrarDialogoEliminarCuenta = true }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        FloatingBottomBar(
            selectedRoute = Screen.CamaraFoto.route,
            onNavigate = { screen -> mainViewModel.navigateBottomTab(screen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PinterestSectionHeader(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.5.sp
        ),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f),
        modifier = Modifier.padding(start = 2.dp, top = 10.dp, bottom = 2.dp)
    )
}

@Composable
private fun PinterestSettingsItem(
    titulo: String,
    subtitulo: String? = null,
    isDestructive: Boolean = false,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp, horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.5.sp
                ),
                color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
            )
            if (subtitulo != null) {
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
                )
            }
        }

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.40f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp
        ),
        color = MartiniGreen,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
private fun SettingsSwitchRow(
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (enabled) 1f else 0.45f)
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (enabled) 0.65f else 0.38f)
            )
        }
        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = { onCheckedChange() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MartiniGreen
            )
        )
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    iconTint: Color,
    titulo: String,
    subtitulo: String? = null,
    badgeText: String? = null,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(21.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                if (subtitulo != null) {
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MartiniGreenContainer.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MartiniGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun FaqItem(pregunta: String, respuesta: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = pregunta,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = respuesta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
        )
    }
}

private fun calcularBytesDirectorio(dir: File?): Long {
    if (dir == null || !dir.exists()) return 0L
    return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
}

private fun limpiarArchivosTemporales(cacheDir: File?) {
    cacheDir?.listFiles()?.forEach { archivo ->
        if (archivo.isFile) {
            archivo.delete()
        }
    }
}

private fun formatearTamanoCache(bytes: Long): String {
    return when {
        bytes <= 0L -> "0 KB"
        bytes < 1024L * 1024L -> "${(bytes / 1024L).coerceAtLeast(1L)} KB"
        else -> String.format("%.1f MB", bytes / (1024f * 1024f))
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilFotoScreenPreview() {
    AppMasterMartini_Grupo9Theme {
        PerfilFotoScreen()
    }
}
