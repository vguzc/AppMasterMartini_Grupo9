package com.example.appmastermartini_grupo9.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Videocam
import com.example.appmastermartini_grupo9.ui.components.MartiniVideoPlayer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel

/**
 * =========================================================================
 * EL "+" SUPERIOR: SELECTOR ENTRE SUBIR RECETA O PUBLICAR EN COMUNIDAD
 * - Opción 1: Subir receta → se publica en "Descubre" y en "Subidas" del perfil.
 * - Opción 2: Hacer consulta / post → se publica en el Foro de "Comunidad".
 * =========================================================================
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CrearRecetaScreen(
    viewModel: MainViewModel = viewModel(),
    onClose: () -> Unit = { viewModel.navigateBack() }
) {
    val context = LocalContext.current
    val borrador by viewModel.borradorReceta.collectAsState()
    val recetasExistentes by viewModel.recetasOficiales.collectAsState()
    val sesionActiva by viewModel.sesionActiva.collectAsState()
    val nombreUsuarioActual by viewModel.nombreUsuario.collectAsState()
    val correoUsuarioActual by viewModel.correoUsuario.collectAsState()

    var errorValidacion by remember { mutableStateOf<String?>(null) }
    var pedirInicioSesion by remember { mutableStateOf(false) }
    var recetaRecienPublicadaId by remember { mutableIntStateOf(-1) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val portadasSugeridas = remember {
        listOf(
            R.drawable.receta_cioccolato to "Chocolatería",
            R.drawable.receta_pasteleria to "Pastelería",
            R.drawable.receta_frozen to "Bollería",
            R.drawable.receta_gelato to "Gelato",
            R.drawable.receta_ricette_hero to "Entremet"
        )
    }

    val categorias = listOf("Pastelería", "Chocolatería", "Bollería", "Gelato", "Salados")
    val nombresProductos = remember { viewModel.catalogoProductos.map { it.name } }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.actualizarFotoBorrador(uri, borrador.imagenRes)
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && tempCameraUri != null) {
            viewModel.actualizarFotoBorrador(tempCameraUri, borrador.imagenRes)
        }
    }
    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.actualizarVideoBorrador(uri)
        }
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val uri = crearUriTemporal(context)
            tempCameraUri = uri
            camaraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso de cámara requerido para tomar foto", Toast.LENGTH_SHORT).show()
        }
    }

    fun abrirCamaraSegura() {
        val tienePermiso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (tienePermiso) {
            val uri = crearUriTemporal(context)
            tempCameraUri = uri
            camaraLauncher.launch(uri)
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Modal de acceso si el usuario cerró sesión, preservando intacto el borrador
    if (pedirInicioSesion) {
        var inputNombre by remember { mutableStateOf(nombreUsuarioActual) }
        var inputCorreo by remember { mutableStateOf(correoUsuarioActual) }
        var errorLogin by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { pedirInicioSesion = false },
            title = {
                Text(
                    text = "Inicia sesión para publicar",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Tu texto se ha conservado. Confirma tus datos para asociar esta publicación a tu perfil.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                    )
                    OutlinedTextField(
                        value = inputNombre,
                        onValueChange = {
                            inputNombre = it
                            errorLogin = null
                        },
                        label = { Text("Tu nombre o alias gastronómico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = inputCorreo,
                        onValueChange = {
                            inputCorreo = it
                            errorLogin = null
                        },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorLogin != null) {
                        Text(
                            text = errorLogin!!,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputNombre.isBlank() || !inputCorreo.contains("@")) {
                            errorLogin = "Ingresa un nombre y un correo válido."
                        } else {
                            viewModel.iniciarSesionConDatos(inputNombre, inputCorreo)
                            pedirInicioSesion = false
                            recetaRecienPublicadaId = viewModel.publicarRecetaDesdeBorrador()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Acceder y publicar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { pedirInicioSesion = false }) {
                    Text("Volver")
                }
            }
        )
    }

    // Confirmación tras subir una Receta a Descubre
    if (recetaRecienPublicadaId != -1) {
        AlertDialog(
            onDismissRequest = {
                val targetId = recetaRecienPublicadaId
                recetaRecienPublicadaId = -1
                onClose()
                viewModel.navigateTo(Screen.RecetaDetalle(targetId))
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MartiniGreen,
                    modifier = Modifier.size(38.dp)
                )
            },
            title = {
                Text(
                    text = "¡Receta subida a Descubre!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Tu preparación ya aparece en el mosaico de Descubre y en la pestaña Subidas de tu perfil.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetId = recetaRecienPublicadaId
                        recetaRecienPublicadaId = -1
                        onClose()
                        viewModel.navigateTo(Screen.RecetaDetalle(targetId))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Abrir receta", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        recetaRecienPublicadaId = -1
                        onClose()
                        viewModel.navigateBottomTab(Screen.Home)
                    }
                ) {
                    Text("Ver en Descubre", color = MartiniGreen)
                }
            }
        )
    }

    // =========================================================================
    // FORMULARIO DIRECTO PARA SUBIR RECETA
    // =========================================================================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Cabecera
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    onClick = onClose,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(40.dp)
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
                    text = "Subir receta",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButtonOnClose(onClose = onClose)
        }

        // =================================================================
        // FORMULARIO: SUBIR RECETA (VA A DESCUBRE Y A SUBIDAS DEL PERFIL)
        // =================================================================
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(PaddingValues(horizontal = 20.dp, vertical = 10.dp)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Fotografía",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        if (borrador.imagenUri != null) {
                            AsyncImage(
                                model = borrador.imagenUri,
                                contentDescription = "Foto seleccionada",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            val res = if (borrador.imagenRes != 0) borrador.imagenRes else R.drawable.receta_cioccolato
                            Image(
                                painter = painterResource(id = res),
                                contentDescription = "Portada de receta",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                onClick = { galeriaLauncher.launch("image/*") },
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.92f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoLibrary,
                                        contentDescription = null,
                                        tint = MartiniGreenDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Galería",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.sp
                                        ),
                                        color = MartiniGreenDark
                                    )
                                }
                            }

                            Surface(
                                onClick = { abrirCamaraSegura() },
                                shape = RoundedCornerShape(50),
                                color = MartiniGreen
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Cámara",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.sp
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        portadasSugeridas.forEach { (resId, etiqueta) ->
                            val seleccionada = borrador.imagenUri == null && borrador.imagenRes == resId
                            Box(
                                modifier = Modifier
                                    .size(width = 76.dp, height = 52.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (seleccionada) 2.5.dp else 0.dp,
                                        color = if (seleccionada) MartiniGreen else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        viewModel.actualizarFotoBorrador(null, resId)
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = etiqueta,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }

                // SECCION: VIDEO CLIP OPCIONAL
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Video de preparacion (Opcional)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (borrador.videoUri != null) {
                            Text(
                                text = "Quitar video",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.clickable { viewModel.limpiarVideoBorrador() }
                            )
                        }
                    }

                    if (borrador.videoUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Black)
                        ) {
                            MartiniVideoPlayer(
                                videoUri = borrador.videoUri,
                                autoPlay = true,
                                initialMuted = true,
                                contentDescription = "Video seleccionado",
                                modifier = Modifier.fillMaxSize()
                            )

                            Surface(
                                onClick = { viewModel.limpiarVideoBorrador() },
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(10.dp)
                                    .size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Quitar video",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Surface(
                            onClick = { videoLauncher.launch("video/*") },
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MartiniGreenContainer,
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Videocam,
                                            contentDescription = null,
                                            tint = MartiniGreen,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Subir clip de video (.mp4)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Muestra el punto de templado, textura o acabado final",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = MartiniGreen
                                ) {
                                    Text(
                                        text = "Elegir",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = borrador.nombre,
                    onValueChange = { nuevo ->
                        errorValidacion = null
                        viewModel.actualizarBorrador { it.copy(nombre = nuevo) }
                    },
                    label = { Text("Nombre de la receta *") },
                    placeholder = { Text("Ej: Tartaleta Gianduja & Avellana") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MartiniGreen,
                        focusedLabelColor = MartiniGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = borrador.descripcion,
                    onValueChange = { nuevo ->
                        errorValidacion = null
                        viewModel.actualizarBorrador { it.copy(descripcion = nuevo) }
                    },
                    label = { Text("Descripción breve *") },
                    placeholder = { Text("Cuenta textura, acabado o consejo clave...") },
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MartiniGreen,
                        focusedLabelColor = MartiniGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = borrador.tiempo,
                        onValueChange = { nuevo ->
                            viewModel.actualizarBorrador { it.copy(tiempo = nuevo) }
                        },
                        label = { Text("Tiempo") },
                        placeholder = { Text("40'") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.width(105.dp)
                    )

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categorias.forEach { cat ->
                            val activa = borrador.categoria == cat
                            FilterChip(
                                selected = activa,
                                onClick = {
                                    viewModel.actualizarBorrador { it.copy(categoria = cat) }
                                },
                                label = { Text(cat) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MartiniGreen,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = borrador.ingredientes,
                    onValueChange = { nuevo ->
                        errorValidacion = null
                        viewModel.actualizarBorrador { it.copy(ingredientes = nuevo) }
                    },
                    label = { Text("Ingredientes (uno por línea) *") },
                    placeholder = {
                        Text("300 g Ariba Chocolate Blanco\n400 ml Decor Up fría...")
                    },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MartiniGreen,
                        focusedLabelColor = MartiniGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = borrador.pasos,
                    onValueChange = { nuevo ->
                        errorValidacion = null
                        viewModel.actualizarBorrador { it.copy(pasos = nuevo) }
                    },
                    label = { Text("Pasos de preparación (uno por línea) *") },
                    placeholder = {
                        Text("Fundir Ariba a 42°C.\nIncorporar Decor Up semimontada...")
                    },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MartiniGreen,
                        focusedLabelColor = MartiniGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Insumos Master Martini utilizados",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        nombresProductos.forEach { nombreProd ->
                            val marcado = borrador.productosSeleccionados.contains(nombreProd)
                            FilterChip(
                                selected = marcado,
                                onClick = {
                                    viewModel.actualizarBorrador { actual ->
                                        val nuevoSet = if (marcado) {
                                            actual.productosSeleccionados - nombreProd
                                        } else {
                                            actual.productosSeleccionados + nombreProd
                                        }
                                        actual.copy(productosSeleccionados = nuevoSet)
                                    }
                                },
                                leadingIcon = if (marcado) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                } else null,
                                label = { Text(nombreProd, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MartiniGreenContainer,
                                    selectedLabelColor = MartiniGreenDark
                                )
                            )
                        }
                    }
                }

                if (errorValidacion != null) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorValidacion!!,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        when {
                            borrador.nombre.trim().length < 4 -> {
                                errorValidacion = "Ingresa un nombre descriptivo para la receta."
                            }
                            borrador.descripcion.trim().length < 8 -> {
                                errorValidacion = "Agrega una breve descripción de tu receta."
                            }
                            borrador.ingredientes.trim().isEmpty() -> {
                                errorValidacion = "Indica al menos un ingrediente."
                            }
                            borrador.pasos.trim().isEmpty() -> {
                                errorValidacion = "Describe los pasos de preparación."
                            }
                            !sesionActiva -> {
                                pedirInicioSesion = true
                            }
                            else -> {
                                recetaRecienPublicadaId = viewModel.publicarRecetaDesdeBorrador()
                            }
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .height(52.dp)
                ) {
                    Text(
                        text = "Publicar receta en Descubre",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
    }
}

@Composable
private fun IconButtonOnClose(onClose: () -> Unit) {
    Surface(
        onClick = onClose,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.size(36.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
