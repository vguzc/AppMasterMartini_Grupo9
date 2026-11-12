package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp

@Composable
fun FontSizeOnboardingScreen(
    escalaActual: Float = 1.0f,
    onConfirmarEscala: (Float) -> Unit,
    onOmitir: () -> Unit
) {
    var escalaSeleccionada by rememberSaveable { mutableStateOf(escalaActual) }
    val opciones = listOf(
        "Pequeña" to 0.85f,
        "Normal" to 1.0f,
        "Grande" to 1.2f,
        "Muy grande" to 1.38f
    )

    Box(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.widthIn(max = 480.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Tamaño de letra", style = MaterialTheme.typography.headlineMedium)

            Column(
                modifier = Modifier.fillMaxWidth().selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                opciones.forEach { (etiqueta, factor) ->
                    val seleccionada = escalaSeleccionada == factor
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (seleccionada) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (seleccionada) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp)
                                .selectable(
                                    selected = seleccionada,
                                    role = Role.RadioButton,
                                    onClick = { escalaSeleccionada = factor }
                                )
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = seleccionada, onClick = null)
                            Text(
                                etiqueta,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "Aa",
                                fontSize = MaterialTheme.typography.titleLarge.fontSize * factor,
                                modifier = Modifier.clearAndSetSemantics {}
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { onConfirmarEscala(escalaSeleccionada) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
            ) {
                Text("Continuar")
            }

            TextButton(
                onClick = onOmitir,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            ) {
                Text("Omitir")
            }
        }
    }
}
