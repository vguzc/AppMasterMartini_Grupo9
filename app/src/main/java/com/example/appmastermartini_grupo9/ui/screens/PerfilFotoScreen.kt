package com.example.appmastermartini_grupo9.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmastermartini_grupo9.ui.components.ImagenInteligente
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.viewmodel.PerfilViewModel
import java.io.File

/**
 * Función auxiliar para generar un URI temporal seguro a través de FileProvider (Guía 13)
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

/**
 * =========================================================================
 * GUÍA 13: PANTALLA DE PERFIL CON INTEGRACIÓN NATIVA DE CÁMARA Y GALERÍA
 * Master Martini Chile - Perfil de Usuario / Colaborador
 * =========================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFotoScreen(
    viewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsState()

    // Variable para almacenar el Uri temporal al tomar una foto con la cámara
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // 1. Lanzador para la Galería (GetContent)
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // No crashea si el usuario cancela la acción
        uri?.let { viewModel.actualizarImagen(it) }
    }

    // 2. Lanzador para la Cámara (TakePicture)
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito: Boolean ->
        // Si el usuario tomó la foto y aceptó
        if (exito && tempCameraUri != null) {
            viewModel.actualizarImagen(tempCameraUri)
        }
    }

    // Lanzador para solicitar permiso de cámara en tiempo de ejecución si se requiere
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido: Boolean ->
        if (concedido) {
            val uri = crearUriTemporal(context)
            tempCameraUri = uri
            camaraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara para capturar fotos", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foto de Perfil Master Martini") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Identificación de Usuario Profesional",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Captura tu fotografía con la cámara del dispositivo o selecciona una desde tu galería.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Componente ImagenInteligente al centro (Guía 13 Parte 3 y 4)
            ImagenInteligente(
                imagenUri = imagenUri,
                tamano = 180.dp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón 1: Abrir Galería
            Button(
                onClick = { galeriaLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = "Galería")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Seleccionar de Galería", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón 2: Activar Cámara
            Button(
                onClick = {
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
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Capturar con Cámara", color = MaterialTheme.colorScheme.onSecondary)
            }

            if (imagenUri != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { viewModel.limpiarImagen() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Quitar foto", tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Quitar Foto", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilFotoScreenPreview() {
    AppMasterMartini_Grupo9Theme {
        PerfilFotoScreen()
    }
}
