package com.example.appmastermartini_grupo9.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmastermartini_grupo9.model.CursoGastronomico
import com.example.appmastermartini_grupo9.model.EspecialidadCurso
import com.example.appmastermartini_grupo9.model.LeccionCurso
import com.example.appmastermartini_grupo9.ui.components.MartiniTopHeader
import com.example.appmastermartini_grupo9.ui.components.MartiniVideoPlayer
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import java.text.Normalizer

@Composable
fun PantallaAnimaciones(mainViewModel: MainViewModel = viewModel()) {
    val cursos by mainViewModel.cursos.collectAsState()
    val inscritos by mainViewModel.cursosCompradosIds.collectAsState()
    val suscritoPro by mainViewModel.suscritoAcademyPro.collectAsState()
    var cursoId by rememberSaveable { mutableStateOf<Int?>(null) }
    var leccionId by rememberSaveable { mutableStateOf<Int?>(null) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var especialidad by rememberSaveable { mutableStateOf<EspecialidadCurso?>(null) }

    val curso = cursos.firstOrNull { it.id == cursoId }
    val leccion = curso?.lecciones?.firstOrNull { it.id == leccionId }
    val volver = {
        if (leccionId != null) leccionId = null else cursoId = null
    }

    BackHandler(enabled = curso != null, onBack = volver)

    when {
        curso != null && leccion != null -> {
            val siguiente = curso.lecciones.getOrNull(curso.lecciones.indexOf(leccion) + 1)
            PantallaLeccion(
                curso = curso,
                leccion = leccion,
                onBack = volver,
                onCompletar = { mainViewModel.completarLeccion(curso.id, leccion.id) },
                onSiguiente = { leccionId = siguiente?.id },
                tieneSiguiente = siguiente != null
            )
        }
        curso != null -> DetalleCurso(
            curso = curso,
            tieneAcceso = curso.id in inscritos || (!curso.esGratuito && suscritoPro),
            onBack = volver,
            onInscribir = {
                mainViewModel.inscribirCurso(curso.id)
                leccionId = curso.lecciones.firstOrNull()?.id
            },
            onComprar = { activarPro ->
                if (activarPro && !suscritoPro) mainViewModel.toggleSuscripcionPro()
                mainViewModel.comprarCurso(curso.id)
            },
            onAbrirLeccion = {
                if (curso.id !in inscritos) mainViewModel.inscribirCurso(curso.id)
                leccionId = it.id
            }
        )
        else -> CatalogoCursos(
            cursos = cursos,
            inscritos = inscritos,
            suscritoPro = suscritoPro,
            busqueda = busqueda,
            onBusqueda = { busqueda = it },
            especialidad = especialidad,
            onEspecialidad = { especialidad = it },
            onBack = { mainViewModel.navigateBack() },
            onAbrir = { cursoId = it.id },
            onContinuar = { cursoEnProgreso ->
                cursoId = cursoEnProgreso.id
                leccionId = cursoEnProgreso.lecciones.firstOrNull { !it.completada }?.id
            }
        )
    }
}

@Composable
private fun CatalogoCursos(
    cursos: List<CursoGastronomico>,
    inscritos: Set<Int>,
    suscritoPro: Boolean,
    busqueda: String,
    onBusqueda: (String) -> Unit,
    especialidad: EspecialidadCurso?,
    onEspecialidad: (EspecialidadCurso?) -> Unit,
    onBack: () -> Unit,
    onAbrir: (CursoGastronomico) -> Unit,
    onContinuar: (CursoGastronomico) -> Unit
) {
    val cursosEnProgreso = cursos.filter { curso ->
        curso.id in inscritos && curso.lecciones.any { !it.completada }
    }
    val consulta = normalizarBusqueda(busqueda.trim())
    val cursosFiltrados = cursos.filter { curso ->
        val coincideEspecialidad = especialidad == null || especialidad in curso.especialidades
        val campos = listOf(curso.titulo, curso.chef, curso.insumoClave) +
            curso.especialidades.map { it.etiqueta }
        coincideEspecialidad && (consulta.isEmpty() || campos.any { normalizarBusqueda(it).contains(consulta) })
    }

    ColumnaCursos {
        MartiniTopHeader(title = "Cursos", onBackClick = onBack)

        if (cursosEnProgreso.isNotEmpty()) {
            Text("Mis cursos en progreso", style = MaterialTheme.typography.titleLarge)
            cursosEnProgreso.forEach { curso ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(curso.titulo, style = MaterialTheme.typography.titleLarge)
                        ProgresoCurso(curso)
                        BotonCurso("Continuar curso") { onContinuar(curso) }
                    }
                }
            }
        }

        Text("Catálogo de cursos", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = busqueda,
            onValueChange = onBusqueda,
            label = { Text("Buscar cursos") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        FiltroEspecialidad(especialidad, onEspecialidad)
        if (cursosFiltrados.isEmpty()) {
            Text(
                if (especialidad != null && cursos.none { especialidad in it.especialidades }) {
                    "Todavía no hay cursos de ${especialidad.etiqueta}."
                } else {
                    "No encontramos cursos con estos filtros."
                }
            )
        }
        cursosFiltrados.forEach { curso ->
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                PortadaCurso(curso)
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(curso.titulo, style = MaterialTheme.typography.titleLarge)
                    Text(
                        curso.especialidades.joinToString(" · ") { it.etiqueta },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(curso.duracion, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        when {
                            curso.id in inscritos || (!curso.esGratuito && suscritoPro) -> "Con acceso"
                            curso.esGratuito -> "Gratis"
                            else -> curso.precioTexto
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    BotonCurso("Ver curso") { onAbrir(curso) }
                }
            }
        }
    }
}

private val marcasDiacriticas = Regex("\\p{M}+")

private fun normalizarBusqueda(texto: String): String =
    Normalizer.normalize(texto, Normalizer.Form.NFD).replace(marcasDiacriticas, "").lowercase()

@Composable
private fun FiltroEspecialidad(
    especialidad: EspecialidadCurso?,
    onEspecialidad: (EspecialidadCurso?) -> Unit
) {
    var abierto by rememberSaveable { mutableStateOf(false) }
    val opciones = listOf(null) + EspecialidadCurso.entries

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { abierto = true },
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) {
            Text(
                text = especialidad?.let { "Especialidad: ${it.etiqueta}" }
                    ?: "Especialidad: Todas",
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion?.etiqueta ?: "Todas las especialidades") },
                    modifier = Modifier.heightIn(min = 52.dp),
                    onClick = {
                        onEspecialidad(opcion)
                        abierto = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DetalleCurso(
    curso: CursoGastronomico,
    tieneAcceso: Boolean,
    onBack: () -> Unit,
    onInscribir: () -> Unit,
    onComprar: (Boolean) -> Unit,
    onAbrirLeccion: (LeccionCurso) -> Unit
) {
    var mostrarCompra by rememberSaveable(curso.id) { mutableStateOf(false) }
    val pendiente = curso.lecciones.firstOrNull { !it.completada }

    ColumnaCursos {
        MartiniTopHeader(title = "Detalle del curso", onBackClick = onBack)
        Card(shape = RoundedCornerShape(20.dp)) { PortadaCurso(curso) }
        Text(curso.titulo, style = MaterialTheme.typography.headlineSmall)
        Text(curso.resumen, style = MaterialTheme.typography.bodyLarge)
        Text("${curso.nivel} · ${curso.duracion} · ${curso.chef}")

        when {
            tieneAcceso -> {
                ProgresoCurso(curso)
                if (pendiente != null) {
                    BotonCurso("Continuar curso") { onAbrirLeccion(pendiente) }
                }
            }
            curso.esGratuito -> BotonCurso("Empezar curso") { onInscribir() }
            else -> BotonCurso("Comprar curso · ${curso.precioTexto}") { mostrarCompra = true }
        }

        Text("Lecciones", style = MaterialTheme.typography.titleLarge)
        curso.lecciones.forEach { leccion ->
            val estado = when {
                !tieneAcceso -> "Requiere inscripción"
                leccion.completada -> "Completada"
                else -> "Pendiente"
            }
            if (tieneAcceso) {
                Surface(
                    onClick = { onAbrirLeccion(leccion) },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    FilaLeccion(leccion, estado)
                }
            } else {
                Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    FilaLeccion(leccion, estado)
                }
            }
        }
    }

    if (mostrarCompra) {
        DialogoCompraCurso(
            curso = curso,
            onDismiss = { mostrarCompra = false },
            onConfirmarCompra = {
                onComprar(false)
                mostrarCompra = false
            },
            onActivarMembresiaPro = {
                onComprar(true)
                mostrarCompra = false
            }
        )
    }
}

@Composable
private fun FilaLeccion(leccion: LeccionCurso, estado: String) {
    Column(
        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("${leccion.numero}. ${leccion.titulo}", style = MaterialTheme.typography.titleMedium)
        Text("${leccion.duracionTexto} · $estado", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun PantallaLeccion(
    curso: CursoGastronomico,
    leccion: LeccionCurso,
    onBack: () -> Unit,
    onCompletar: () -> Unit,
    onSiguiente: () -> Unit,
    tieneSiguiente: Boolean
) {
    ColumnaCursos(scrollKey = leccion.id) {
        MartiniTopHeader(title = "Lección ${leccion.numero}", onBackClick = onBack)
        Text(curso.titulo, style = MaterialTheme.typography.titleMedium)
        Text(leccion.titulo, style = MaterialTheme.typography.headlineSmall)
        Text(leccion.duracionTexto, style = MaterialTheme.typography.bodyLarge)
        ProgresoCurso(curso)

        if (leccion.videoRes != 0 || leccion.videoUri != null) {
            MartiniVideoPlayer(
                modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                videoRes = leccion.videoRes,
                videoUri = leccion.videoUri,
                autoPlay = false,
                contentDescription = "Video de la lección"
            )
        }
        if (leccion.contenido.isNotBlank()) {
            Text(leccion.contenido, style = MaterialTheme.typography.bodyLarge)
        } else if (leccion.videoRes == 0 && leccion.videoUri == null) {
            Text("El material de esta lección aún está en preparación.")
        }

        if (leccion.completada) {
            Text("Lección completada", style = MaterialTheme.typography.titleMedium)
        } else {
            BotonCurso("Marcar como completada") { onCompletar() }
        }

        OutlinedButton(
            onClick = if (tieneSiguiente) onSiguiente else onBack,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) {
            Text(if (tieneSiguiente) "Siguiente lección" else "Volver al curso")
        }
    }
}

@Composable
private fun ColumnaCursos(
    scrollKey: Int? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = key(scrollKey) { rememberScrollState() }
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxSize()
                .verticalScroll(scrollState).padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
private fun PortadaCurso(curso: CursoGastronomico) {
    Image(
        painter = painterResource(curso.imagenRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
    )
}

@Composable
private fun ProgresoCurso(curso: CursoGastronomico) {
    val total = curso.lecciones.size
    val completadas = curso.lecciones.count { it.completada }
    if (total == 0) return
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("$completadas de $total lecciones completadas", style = MaterialTheme.typography.bodyLarge)
        LinearProgressIndicator(
            progress = { completadas.toFloat() / total },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BotonCurso(texto: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
        Text(texto, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun DialogoCompraCurso(
    curso: CursoGastronomico,
    onDismiss: () -> Unit,
    onConfirmarCompra: () -> Unit,
    onActivarMembresiaPro: () -> Unit
) {
    var opcion by rememberSaveable { mutableStateOf(0) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Acceso al curso") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(curso.titulo, style = MaterialTheme.typography.titleMedium)
                listOf("Curso · ${curso.precioTexto}", "Membresía Academy Pro").forEachIndexed { indice, texto ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = opcion == indice, onClick = { opcion = indice })
                        Text(texto)
                    }
                }
                Text("Compra de demostración.")
            }
        },
        confirmButton = {
            Button(onClick = if (opcion == 0) onConfirmarCompra else onActivarMembresiaPro) {
                Text("Confirmar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
