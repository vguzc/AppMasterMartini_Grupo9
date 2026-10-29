package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.viewmodel.EstadoViewModel

/**
 * =========================================================================
 * GUÍA 12: ANIMACIONES, ESTADOS Y PERSISTENCIA CON DATASTORE PREFERENCES
 * Modula el estado del modo especial / cliente profesional Master Martini
 * =========================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAnimaciones(
    modifier: Modifier = Modifier,
    viewModel: EstadoViewModel = viewModel()
) {
    val estado by viewModel.activo.collectAsState()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modo Profesional & Animaciones") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        // Si el estado es null, simulamos carga mostrando CircularProgressIndicator (Guía 12 Parte 3)
        if (estado == null) {
            Box(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(54.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Cargando preferencias desde DataStore...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            val estaActivo = estado!!

            // Animación de color del botón según el estado (Guía 12)
            val colorAnimado by animateColorAsState(
                targetValue = if (estaActivo) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary,
                animationSpec = tween(durationMillis = 500),
                label = "colorBotonAnimado"
            )

            // Texto derivado con derivedStateOf (Guía 12)
            val textoBoton by remember(estaActivo) {
                derivedStateOf {
                    if (estaActivo) "Desactivar Modo Profesional" else "Activar Modo Profesional"
                }
            }

            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gestión de Estado Persistente",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (estaActivo)
                        "El Modo Profesional Master Martini está ACTIVO (Precios mayoristas habilitados)."
                    else
                        "El Modo Profesional Master Martini está DESACTIVADO.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Botón interactivo con color animado y texto derivado
                Button(
                    onClick = { viewModel.alternarEstado() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(
                        text = textoBoton,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Feedback visual animado con AnimatedVisibility (Guía 12 Parte 4)
                AnimatedVisibility(visible = mostrarMensaje) {
                    Text(
                        text = "¡Estado guardado exitosamente en DataStore!",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaAnimacionesPreview() {
    AppMasterMartini_Grupo9Theme {
        PantallaAnimaciones()
    }
}
