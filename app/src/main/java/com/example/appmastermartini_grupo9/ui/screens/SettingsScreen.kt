package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel

/**
 * =========================================================================
 * GUÍA 10: PANTALLA DE CONFIGURACIÓN E INFORMACIÓN CORPORATIVA
 * Master Martini Chile - Insumos para Panadería, Pastelería y Chocolatería
 * =========================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController = rememberNavController(),
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información y Configuración") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        // Estructura visual centralizada según requerimientos de la Guía 10
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título o texto principal
            Text(
                text = "Master Martini Chile / Grupo Unigrà",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta con datos corporativos oficiales
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🏢 Casa Matriz & Centro de Distribución:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Avenida Américo Vespucio 1385, Centro SpacioFlex, Módulo 3, Quilicura, Región Metropolitana, Chile.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "📞 Atención a Clientes:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "+56 2 26611700\nclientes@mastermartini.cl",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "🌐 Sitio Web:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "https://mastermartini.cl/",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para volver al Inicio (Home)
            Button(
                onClick = { viewModel.navigateTo(Screen.Home) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ir al Catálogo / Perfil
            Button(
                onClick = { viewModel.navigateTo(Screen.Profile) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir al Catálogo de Productos", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AppMasterMartini_Grupo9Theme {
        SettingsScreen()
    }
}
