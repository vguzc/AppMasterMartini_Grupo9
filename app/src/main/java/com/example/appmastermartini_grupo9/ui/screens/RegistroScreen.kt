package com.example.appmastermartini_grupo9.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmastermartini_grupo9.viewmodel.UsuarioViewModel

/**
 * =========================================================================
 * GUÍA 11: PANTALLA DE REGISTRO CON FORMULARIO REACTIVO Y VALIDACIONES
 * Master Martini Chile - Portal de Clientes y Distribuidores
 * =========================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro Cliente Master Martini") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Formulario de Alta de Cliente Profesional",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Ingresa tus datos para acceder a precios mayoristas y catálogo técnico.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Campo Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre o Razón Social *") },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Correo Electrónico
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo Electrónico *") },
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Clave
            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Contraseña (mínimo 6 caracteres) *") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = {
                    estado.errores.clave?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Dirección
            OutlinedTextField(
                value = estado.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = { Text("Dirección de Despacho / Local *") },
                isError = estado.errores.direccion != null,
                supportingText = {
                    estado.errores.direccion?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Checkbox: Aceptar Términos y Condiciones
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::onAceptarTerminosChange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Acepto los términos y condiciones comerciales de Master Martini Chile.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Envío
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        navController.navigate("resumen")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
