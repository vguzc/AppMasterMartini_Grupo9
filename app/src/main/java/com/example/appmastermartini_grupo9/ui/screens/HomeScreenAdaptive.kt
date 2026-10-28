package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.ui.utils.obtenerWindowSizeClass

/**
 * =========================================================================
 * GUÍA 9: ADAPTABILIDAD DEL DISEÑO CON WINDOW SIZE CLASSES (MATERIAL 3)
 * Master Martini Chile - Insumos para Panadería, Pastelería y Chocolatería
 * =========================================================================
 */

/**
 * Pantalla adaptativa que selecciona la vista adecuada según el ancho de pantalla del dispositivo.
 */
@Composable
fun HomeScreenAdaptive(onNavigateToCatalog: () -> Unit = {}) {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(onNavigateToCatalog)
        WindowWidthSizeClass.Medium -> HomeScreenMediana(onNavigateToCatalog)
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(onNavigateToCatalog)
        else -> HomeScreenCompacta(onNavigateToCatalog)
    }
}

/**
 * 1. DISEÑO COMPACTO (Smartphones en orientación vertical)
 * Layout centrado en columna vertical con espaciado uniforme.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(onActionClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Master Martini Chile") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo corporativo institucional Master Martini
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Master Martini",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Fit
            )

            // Título principal con estilo tipográfico de MaterialTheme
            Text(
                text = "¡Bienvenidos a Master Martini!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Descripción breve de la empresa
            Text(
                text = "Líder en insumos vegetales para panadería, pastelería y chocolatería profesional. Filial oficial de Unigrà en Chile.",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            // Tarjeta informativa compacta
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "📱 Vista Compacta (Smartphones)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Diseño optimizado para una sola mano y visualización vertical ágil.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Botón de acción con estilo corporativo
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Catálogo de Productos", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}

/**
 * 2. DISEÑO MEDIANO (Tablets pequeñas, plegables o smartphones en apaisado)
 * Layout balanceado que aprovecha el espacio horizontal intermedio.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana(onActionClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Master Martini Chile - Modo Mediano") },
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Master Martini",
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp),
                    contentScale = ContentScale.Fit
                )

                Column(
                    modifier = Modifier.weight(1.2f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Excelencia en Repostería y Chocolatería",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Líneas Ariba, Caravella, Decor Up y OraSì para el mercado Horeca y artesanal.",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = onActionClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Explorar Soluciones", color = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💻 Vista Mediana (Foldables / Tablets)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Estructura en dos bloques horizontales para optimizar la legibilidad en pantallas medianas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

/**
 * 3. DISEÑO EXPANDIDO (Tablets grandes, monitores o pantallas de escritorio)
 * Layout en dos columnas completas con panel institucional a la izquierda y catálogo/contacto a la derecha.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida(onActionClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Master Martini Chile - Portal Profesional (Vista Expandida)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Columna Izquierda: Identidad y Marca
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Master Martini",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "Master Martini Chile / Grupo Unigrà",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Centro de Operaciones: Av. Américo Vespucio 1385, Quilicura, Santiago.\nTeléfono: +56 2 26611700\nCorreo: clientes@mastermartini.cl",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Columna Derecha: Contenido y Acciones ampliadas
            Card(
                modifier = Modifier
                    .weight(1.4f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "🖥️ Vista Expandida (Tablets & Desktop)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Aprovecha al máximo el ancho de pantalla dividiendo la interfaz en panel maestro y panel de contenido.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Líneas de Producción Destacadas:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "• Ariba: Coberturas y chocolates puros con cacao de Costa de Marfil.\n" +
                               "• Caravella: Cremas de avellana, cacao y rellenos resistentes al horneado.\n" +
                               "• Decor Up: Cremas UHT de alta montabilidad y brillo.\n" +
                               "• OraSì: Bebidas vegetales para cafetería y pastelería vegana.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.weight(1f, fill = false))
                    Button(
                        onClick = onActionClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Acceder al Catálogo Completo", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

// =========================================================================
// PREVIEWS REQUERIDAS POR GUÍA 9
// =========================================================================

@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewCompact() {
    AppMasterMartini_Grupo9Theme {
        HomeScreenCompacta()
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun PreviewMediana() {
    AppMasterMartini_Grupo9Theme {
        HomeScreenMediana()
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 800, showBackground = true)
@Composable
fun PreviewExpandida() {
    AppMasterMartini_Grupo9Theme {
        HomeScreenExpandida()
    }
}
