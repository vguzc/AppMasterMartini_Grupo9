package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmastermartini_grupo9.viewmodel.UsuarioViewModel

/**
 * =========================================================================
 * GUÍA 11: PANTALLA DE RESUMEN DE DATOS INGRESADOS
 * Observa el mismo UsuarioViewModel compartido en tiempo real con collectAsState
 * =========================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    navController: NavController? = null,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen del Registro") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Resumen del Registro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Nombre / Razón Social: ${estado.nombre}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Correo: ${estado.correo}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Dirección: ${estado.direccion}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Contraseña: ${"*".repeat(estado.clave.length)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (estado.aceptaTerminos) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            navController?.let { nav ->
                Button(
                    onClick = { nav.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar Datos", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}
