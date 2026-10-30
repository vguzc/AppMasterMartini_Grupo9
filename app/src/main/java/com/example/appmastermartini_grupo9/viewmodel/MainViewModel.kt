package com.example.appmastermartini_grupo9.viewmodel

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.model.BorradorReceta
import com.example.appmastermartini_grupo9.model.CarpetaRecetas
import com.example.appmastermartini_grupo9.model.ComentarioComunidad
import com.example.appmastermartini_grupo9.model.CursoGastronomico
import com.example.appmastermartini_grupo9.model.LeccionCurso
import com.example.appmastermartini_grupo9.model.ProductItem
import com.example.appmastermartini_grupo9.model.PublicacionComunidad
import com.example.appmastermartini_grupo9.model.RecetaOficial
import com.example.appmastermartini_grupo9.navigation.NavigationEvent
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel principal unificado (Arquitectura MVVM Master Martini).
 * - "Descubre": reúne todas las recetas (Oficiales Master Martini con sello verificado + subidas por usuarios).
 * - "Comunidad": foro/muro real de pasteleros (consultas técnicas, datos de oficio y "Así me quedó").
 * - "+" Crear: permite elegir entre subir una receta (va a Descubre + Subidas) o publicar en el foro (va a Comunidad).
 */
class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    val catalogoProductos: List<ProductItem> = ProductRepository().getFeaturedProducts()

    // =========================================================================
    // 1. SESIÓN DE USUARIO Y BORRADOR DE CREACIÓN
    // =========================================================================
    private val _sesionActiva = MutableStateFlow(true)
    val sesionActiva: StateFlow<Boolean> = _sesionActiva.asStateFlow()

    private val _nombreUsuario = MutableStateFlow("Valentina Soto")
    val nombreUsuario: StateFlow<String> = _nombreUsuario.asStateFlow()

    private val _handleUsuario = MutableStateFlow("valentinasoto")
    val handleUsuario: StateFlow<String> = _handleUsuario.asStateFlow()

    private val _correoUsuario = MutableStateFlow("valentina.soto@pasteleria.cl")
    val correoUsuario: StateFlow<String> = _correoUsuario.asStateFlow()

    private val _telefonoUsuario = MutableStateFlow("+56 9 8412 3900")
    val telefonoUsuario: StateFlow<String> = _telefonoUsuario.asStateFlow()

    private val _rolUsuario = MutableStateFlow("Pastelería Artesanal • Providencia")
    val rolUsuario: StateFlow<String> = _rolUsuario.asStateFlow()

    // Preferencias pertinentes de la aplicación, notificaciones y privacidad
    private val _unidadesPasteleria = MutableStateFlow("Métrico (g · ml · °C)")
    val unidadesPasteleria: StateFlow<String> = _unidadesPasteleria.asStateFlow()

    private val _idiomaApp = MutableStateFlow("Español (CL)")
    val idiomaApp: StateFlow<String> = _idiomaApp.asStateFlow()

    private val _respuestaHaptica = MutableStateFlow(true)
    val respuestaHaptica: StateFlow<Boolean> = _respuestaHaptica.asStateFlow()

    private val _notifPushGeneral = MutableStateFlow(true)
    val notifPushGeneral: StateFlow<Boolean> = _notifPushGeneral.asStateFlow()

    private val _notifForoComunidad = MutableStateFlow(true)
    val notifForoComunidad: StateFlow<Boolean> = _notifForoComunidad.asStateFlow()

    private val _notifRecetasYCursos = MutableStateFlow(true)
    val notifRecetasYCursos: StateFlow<Boolean> = _notifRecetasYCursos.asStateFlow()

    private val _notifCatalogoProductos = MutableStateFlow(false)
    val notifCatalogoProductos: StateFlow<Boolean> = _notifCatalogoProductos.asStateFlow()

    private val _boletinCorreo = MutableStateFlow(true)
    val boletinCorreo: StateFlow<Boolean> = _boletinCorreo.asStateFlow()

    private val _perfilPublicoComunidad = MutableStateFlow(true)
    val perfilPublicoComunidad: StateFlow<Boolean> = _perfilPublicoComunidad.asStateFlow()

    private val _altaCalidadSoloWifi = MutableStateFlow(true)
    val altaCalidadSoloWifi: StateFlow<Boolean> = _altaCalidadSoloWifi.asStateFlow()

    private val _borradorReceta = MutableStateFlow(
        BorradorReceta(imagenRes = R.drawable.receta_cioccolato)
    )
    val borradorReceta: StateFlow<BorradorReceta> = _borradorReceta.asStateFlow()

    // Modo inicial opcional al tocar "+" (null = mostrar selector, "receta" = ir a receta, "foro" = ir a consulta/post)
    private val _modoCreacionInicial = MutableStateFlow<String?>(null)
    val modoCreacionInicial: StateFlow<String?> = _modoCreacionInicial.asStateFlow()

    // =========================================================================
    // 2. DESCUBRE: RECETAS (OFICIALES MARTINI + USUARIOS) Y COLECCIONES GUARDADAS
    // =========================================================================
    private val _recetasOficiales = MutableStateFlow(crearRecetasIniciales())
    val recetasOficiales: StateFlow<List<RecetaOficial>> = _recetasOficiales.asStateFlow()

    private val _recetasGuardadasIds = MutableStateFlow(setOf(100, 1, 2, 4, 7))
    val recetasGuardadasIds: StateFlow<Set<Int>> = _recetasGuardadasIds.asStateFlow()

    // Colecciones personalizadas de recetas guardadas creadas por el usuario
    private val _carpetasUsuario = MutableStateFlow(crearCarpetasIniciales())
    val carpetasUsuario: StateFlow<List<CarpetaRecetas>> = _carpetasUsuario.asStateFlow()

    // =========================================================================
    // 3. COMUNIDAD: FORO / MURO DE PASTELEROS (SIN RECETAS DUPLICADAS)
    // =========================================================================
    private val _publicacionesComunidad = MutableStateFlow(crearPublicacionesIniciales())
    val publicacionesComunidad: StateFlow<List<PublicacionComunidad>> = _publicacionesComunidad.asStateFlow()

    private val _postsLikeadosIds = MutableStateFlow(setOf(201))
    val postsLikeadosIds: StateFlow<Set<Int>> = _postsLikeadosIds.asStateFlow()

    private val _postsGuardadosIds = MutableStateFlow(setOf<Int>())
    val postsGuardadosIds: StateFlow<Set<Int>> = _postsGuardadosIds.asStateFlow()

    // Threads Logic: Reposts y Me gusta a respuestas individuales
    private val _postsRepostedIds = MutableStateFlow(setOf(202))
    val postsRepostedIds: StateFlow<Set<Int>> = _postsRepostedIds.asStateFlow()

    private val _comentariosLikeadosIds = MutableStateFlow(setOf("201-1", "205-2"))
    val comentariosLikeadosIds: StateFlow<Set<String>> = _comentariosLikeadosIds.asStateFlow()

    // =========================================================================
    // 4. CURSOS Y PROGRESO ("APRENDE" Y "MIS CURSOS")
    // =========================================================================
    private val _cursos = MutableStateFlow(crearCursosIniciales())
    val cursos: StateFlow<List<CursoGastronomico>> = _cursos.asStateFlow()

    private val _progresoCursos = MutableStateFlow(mapOf(1 to 0.35f))
    val progresoCursos: StateFlow<Map<Int, Float>> = _progresoCursos.asStateFlow()

    private val _cursosCompradosIds = MutableStateFlow<Set<Int>>(setOf(1))
    val cursosCompradosIds: StateFlow<Set<Int>> = _cursosCompradosIds.asStateFlow()

    private val _cursoSeleccionadoDetalle = MutableStateFlow<CursoGastronomico?>(null)
    val cursoSeleccionadoDetalle: StateFlow<CursoGastronomico?> = _cursoSeleccionadoDetalle.asStateFlow()

    private val _suscritoAcademyPro = MutableStateFlow(false)
    val suscritoAcademyPro: StateFlow<Boolean> = _suscritoAcademyPro.asStateFlow()

    private val _productoDestacadoId = MutableStateFlow<String?>(null)
    val productoDestacadoId: StateFlow<String?> = _productoDestacadoId.asStateFlow()

    // =========================================================================
    // ACCIONES DE NAVEGACIÓN
    // =========================================================================
    fun navigateTo(screen: Screen) {
        if (screen == Screen.CrearReceta) {
            _modoCreacionInicial.value = null
        }
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun abrirCrearConModo(modo: String? = null) {
        _modoCreacionInicial.value = modo
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = Screen.CrearReceta))
        }
    }

    fun limpiarModoCreacionInicial() {
        _modoCreacionInicial.value = null
    }

    fun navigateBottomTab(screen: Screen) {
        viewModelScope.launch {
            if (screen == Screen.CrearReceta) {
                _modoCreacionInicial.value = null
                _navigationEvents.emit(NavigationEvent.NavigateTo(route = Screen.CrearReceta))
            } else {
                _navigationEvents.emit(
                    NavigationEvent.NavigateTo(
                        route = screen,
                        popUpToRoute = Screen.Home,
                        inclusive = false,
                        singleTop = true
                    )
                )
            }
        }
    }

    fun abrirProductoEnCatalogo(productId: String) {
        _productoDestacadoId.value = productId
        navigateTo(Screen.Profile)
    }

    fun limpiarProductoDestacado() {
        _productoDestacadoId.value = null
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }

    // =========================================================================
    // ACCIONES DE RECETAS EN DESCUBRE Y COLECCIONES GUARDADAS
    // =========================================================================
    fun toggleGuardarReceta(recetaId: Int) {
        _recetasGuardadasIds.update { actual ->
            if (actual.contains(recetaId)) actual - recetaId else actual + recetaId
        }
    }

    fun crearCarpeta(
        nombre: String,
        esPrivada: Boolean = false,
        esGrupo: Boolean = false,
        recetasInicialesIds: List<Int> = emptyList()
    ) {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isEmpty()) return
        val nuevoId = (_carpetasUsuario.value.maxOfOrNull { it.id } ?: 10) + 1
        val nuevaCarpeta = CarpetaRecetas(
            id = nuevoId,
            nombre = nombreLimpio,
            recetasOficialesIds = recetasInicialesIds,
            esPrivada = esPrivada,
            esGrupo = esGrupo,
            actualizadoHace = "Ahora"
        )
        _carpetasUsuario.update { listOf(nuevaCarpeta) + it }
        if (recetasInicialesIds.isNotEmpty()) {
            _recetasGuardadasIds.update { it + recetasInicialesIds }
        }
    }

    fun toggleRecetaEnCarpeta(carpetaId: Int, recetaId: Int) {
        _carpetasUsuario.update { lista ->
            lista.map { carpeta ->
                if (carpeta.id == carpetaId) {
                    val nuevaLista = if (carpeta.recetasOficialesIds.contains(recetaId)) {
                        carpeta.recetasOficialesIds - recetaId
                    } else {
                        carpeta.recetasOficialesIds + recetaId
                    }
                    carpeta.copy(
                        recetasOficialesIds = nuevaLista,
                        actualizadoHace = "Ahora"
                    )
                } else {
                    carpeta
                }
            }
        }
    }

    fun eliminarCarpeta(carpetaId: Int) {
        _carpetasUsuario.update { lista ->
            lista.filterNot { it.id == carpetaId }
        }
    }

    fun obtenerRecetaPorId(recetaId: Int): RecetaOficial? {
        return _recetasOficiales.value.find { it.id == recetaId }
    }

    fun recetasAsociadasAProducto(productId: String): List<RecetaOficial> {
        return _recetasOficiales.value.filter { it.productosAsociadosIds.contains(productId) }
    }

    // =========================================================================
    // ACCIONES DE COMUNIDAD (FORO DE PASTELEROS) Y CONEXIÓN CON RECETAS
    // =========================================================================
    fun toggleLikePost(postId: Int) {
        _postsLikeadosIds.update { actual ->
            if (actual.contains(postId)) actual - postId else actual + postId
        }
    }

    fun toggleRepostPost(postId: Int) {
        _postsRepostedIds.update { actual ->
            if (actual.contains(postId)) actual - postId else actual + postId
        }
    }

    fun toggleGuardarPost(postId: Int) {
        _postsGuardadosIds.update { actual ->
            if (actual.contains(postId)) actual - postId else actual + postId
        }
    }

    fun toggleLikeComentario(postId: Int, comentarioId: Int) {
        val clave = "$postId-$comentarioId"
        _comentariosLikeadosIds.update { actual ->
            if (actual.contains(clave)) actual - clave else actual + clave
        }
    }

    fun obtenerPostPorId(postId: Int): PublicacionComunidad? {
        return _publicacionesComunidad.value.find { it.id == postId }
    }

    fun obtenerHiloDeRecetaOficial(recetaId: Int): PublicacionComunidad? {
        return _publicacionesComunidad.value.find { it.recetaOficialId == recetaId }
    }

    /**
     * Si una receta de Descubre ya tiene un hilo de consulta en Comunidad, devuelve su ID.
     * Si no existe aún, abre un hilo limpio de consulta técnica vinculado a esa receta.
     */
    fun obtenerOCrearHiloDeReceta(recetaId: Int): Int {
        val existente = obtenerHiloDeRecetaOficial(recetaId)
        if (existente != null) return existente.id

        val receta = obtenerRecetaPorId(recetaId) ?: return 201
        val nuevoId = (_publicacionesComunidad.value.maxOfOrNull { it.id } ?: 300) + 1
        val nombresProductos = receta.productosAsociadosIds.mapNotNull { id ->
            catalogoProductos.find { it.id == id }?.name
        }
        val nuevoHilo = PublicacionComunidad(
            id = nuevoId,
            tipoPublicacion = "Comunidad",
            autor = "Mesa Técnica Master Martini",
            rolAutor = "Hilo de consultas y resultados",
            esOficialMartini = true,
            esPropiaUsuario = false,
            haceCuanto = "Hilo abierto",
            titulo = "Consultas y resultados: ${receta.titulo}",
            descripcion = "Comparte cómo te quedó esta preparación o deja tus dudas sobre temperaturas, batido y rendimiento.",
            productosUtilizados = emptyList(),
            imagenRes = 0,
            likesBase = 8,
            repostsBase = 2,
            recetaOficialId = receta.id,
            comentarios = emptyList()
        )
        _publicacionesComunidad.update { listOf(nuevoHilo) + it }
        return nuevoId
    }

    fun agregarComentario(postId: Int, texto: String, parentComentarioId: Int? = null) {
        val limpio = texto.trim()
        if (limpio.isEmpty()) return
        val autorActual = if (_sesionActiva.value) _nombreUsuario.value else "Invitado Master Martini"
        _publicacionesComunidad.update { lista ->
            lista.map { post ->
                if (post.id == postId) {
                    val nuevoComentario = ComentarioComunidad(
                        id = (post.comentarios.maxOfOrNull { it.id } ?: 0) + 1,
                        autor = autorActual,
                        haceCuanto = "Ahora",
                        texto = limpio,
                        esOficialMartini = false,
                        parentComentarioId = parentComentarioId
                    )
                    post.copy(comentarios = post.comentarios + nuevoComentario)
                } else {
                    post
                }
            }
        }
    }

    fun agregarComentarioAReceta(recetaId: Int, texto: String) {
        val hiloId = obtenerOCrearHiloDeReceta(recetaId)
        agregarComentario(hiloId, texto)
    }

    // =========================================================================
    // ACCIONES DE BORRADOR, SESIÓN Y PUBLICACIÓN DESDE "+" (RECETA O FORO)
    // =========================================================================
    fun actualizarBorrador(transform: (BorradorReceta) -> BorradorReceta) {
        _borradorReceta.update(transform)
    }

    fun limpiarBorrador() {
        _borradorReceta.value = BorradorReceta(imagenRes = R.drawable.receta_cioccolato)
    }

    fun iniciarSesionConDatos(nombre: String, correo: String, handle: String = "") {
        val nombreLimpio = nombre.trim().ifBlank { "Valentina Soto" }
        _nombreUsuario.value = nombreLimpio
        _correoUsuario.value = correo.trim()
        val handleLimpio = handle.trim().removePrefix("@").ifBlank {
            nombreLimpio.lowercase().replace(" ", "")
        }
        _handleUsuario.value = handleLimpio
        _sesionActiva.value = true
        sincronizarPublicacionesYRecetasConUsuario(nombreLimpio)
    }

    fun registrarNuevoUsuario(
        nombre: String,
        handle: String,
        correo: String,
        telefono: String,
        especialidad: String = "Pastelería Artesanal"
    ) {
        val nombreLimpio = nombre.trim()
        _nombreUsuario.value = nombreLimpio
        _correoUsuario.value = correo.trim()
        _telefonoUsuario.value = telefono.trim()
        val handleLimpio = handle.trim().removePrefix("@").ifBlank {
            nombreLimpio.lowercase().replace(" ", "")
        }
        _handleUsuario.value = handleLimpio
        _rolUsuario.value = especialidad.trim()
        _sesionActiva.value = true
        sincronizarPublicacionesYRecetasConUsuario(nombreLimpio)
    }

    fun sincronizarPublicacionesYRecetasConUsuario(nombreActual: String) {
        // En publicaciones: es propia únicamente si el autor coincide con el usuario activo
        _publicacionesComunidad.update { lista ->
            lista.map { post ->
                val esMio = nombreActual.isNotBlank() && post.autor.equals(nombreActual, ignoreCase = true)
                post.copy(esPropiaUsuario = esMio)
            }
        }
        // En recetas: es propia únicamente si el autor coincide con el usuario activo
        _recetasOficiales.update { lista ->
            lista.map { receta ->
                val esMia = nombreActual.isNotBlank() && receta.autor.equals(nombreActual, ignoreCase = true)
                receta.copy(esPropiaUsuario = esMia)
            }
        }
    }

    fun actualizarDatosCuenta(
        nombre: String,
        correo: String,
        telefono: String,
        especialidad: String
    ) {
        if (nombre.isNotBlank()) {
            _nombreUsuario.value = nombre.trim()
            _handleUsuario.value = nombre.trim().lowercase().replace(" ", "")
        }
        if (correo.isNotBlank()) _correoUsuario.value = correo.trim()
        if (telefono.isNotBlank()) _telefonoUsuario.value = telefono.trim()
        if (especialidad.isNotBlank()) _rolUsuario.value = especialidad.trim()
        _sesionActiva.value = true
        sincronizarPublicacionesYRecetasConUsuario(_nombreUsuario.value)
    }

    fun cambiarUnidadesPasteleria(unidades: String) {
        _unidadesPasteleria.value = unidades
    }

    fun cambiarIdiomaApp(idioma: String) {
        _idiomaApp.value = idioma
    }

    fun toggleRespuestaHaptica() {
        _respuestaHaptica.update { !it }
    }

    fun toggleNotifPushGeneral() {
        _notifPushGeneral.update { !it }
    }

    fun toggleNotifForoComunidad() {
        _notifForoComunidad.update { !it }
    }

    fun toggleNotifRecetasYCursos() {
        _notifRecetasYCursos.update { !it }
    }

    fun toggleNotifCatalogoProductos() {
        _notifCatalogoProductos.update { !it }
    }

    fun toggleBoletinCorreo() {
        _boletinCorreo.update { !it }
    }

    fun togglePerfilPublicoComunidad() {
        _perfilPublicoComunidad.update { !it }
    }

    fun toggleAltaCalidadSoloWifi() {
        _altaCalidadSoloWifi.update { !it }
    }

    fun cerrarSesion() {
        _sesionActiva.value = false
        sincronizarPublicacionesYRecetasConUsuario("")
    }

    fun eliminarCuentaUsuario() {
        _sesionActiva.value = false
        _nombreUsuario.value = "Invitado"
        _handleUsuario.value = "invitado"
        _correoUsuario.value = ""
        _telefonoUsuario.value = ""
        _rolUsuario.value = "Cuenta sin iniciar"
        _suscritoAcademyPro.value = false
        sincronizarPublicacionesYRecetasConUsuario("")
    }

    /**
     * Publica una receta creada por el usuario directamente en "Descubre"
     * (y queda disponible también en la pestaña "Subidas" de su Perfil).
     * Devuelve el ID de la nueva receta creada.
     */
    fun publicarRecetaDesdeBorrador(): Int {
        val draft = _borradorReceta.value
        val nuevoId = (_recetasOficiales.value.maxOfOrNull { it.id } ?: 100) + 1

        val listaIngredientes = draft.ingredientes
            .lines()
            .map { it.trim().removePrefix("-").removePrefix("•").trim() }
            .filter { it.isNotEmpty() }
            .ifEmpty { listOf("Insumos Master Martini según proporción indicada") }

        val listaPasos = draft.pasos
            .lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .ifEmpty { listOf("Integrar los ingredientes respetando temperaturas de trabajo y montar.") }

        val idsProductosAsociados = draft.productosSeleccionados.mapNotNull { nombreProd ->
            catalogoProductos.find { it.name.equals(nombreProd, ignoreCase = true) }?.id
        }

        val (bgCat, txtCat) = coloresPorCategoria(draft.categoria)

        val nuevaReceta = RecetaOficial(
            id = nuevoId,
            titulo = draft.nombre.trim(),
            subtitulo = draft.descripcion.trim().ifBlank { "Preparación compartida por ${_nombreUsuario.value}." },
            categoria = draft.categoria,
            tiempo = draft.tiempo.trim().ifBlank { "35'" },
            nivel = "Comunidad",
            porciones = "6-8 porciones",
            imagenRes = if (draft.imagenRes != 0) draft.imagenRes else R.drawable.receta_cioccolato,
            imagenUri = draft.imagenUri,
            videoRes = draft.videoRes,
            videoUri = draft.videoUri,
            autor = _nombreUsuario.value,
            esOficialMartini = false,
            esPropiaUsuario = true,
            esRecetaDelDia = false,
            haceCuanto = "Ahora",
            ingredientes = listaIngredientes,
            pasos = listaPasos,
            productosAsociadosIds = idsProductosAsociados,
            colorCategoriaBg = bgCat,
            colorCategoriaTexto = txtCat
        )

        _recetasOficiales.update { listOf(nuevaReceta) + it }
        limpiarBorrador()
        return nuevoId
    }

    /**
     * Publica una consulta técnica, dato de oficio o foto de "Así me quedó"
     * directamente en el Foro de Comunidad.
     */
    fun publicarEnForoComunidad(
        tipoPublicacion: String,
        titulo: String,
        descripcion: String,
        productosSeleccionados: List<String>,
        recetaAsociadaId: Int?,
        imagenUri: Uri?,
        @DrawableRes imagenRes: Int
    ): Int {
        val nuevoId = (_publicacionesComunidad.value.maxOfOrNull { it.id } ?: 300) + 1
        val nuevaPublicacion = PublicacionComunidad(
            id = nuevoId,
            tipoPublicacion = tipoPublicacion,
            autor = _nombreUsuario.value,
            rolAutor = _rolUsuario.value,
            esOficialMartini = false,
            esPropiaUsuario = true,
            haceCuanto = "Ahora",
            titulo = titulo.trim(),
            descripcion = descripcion.trim(),
            productosUtilizados = productosSeleccionados,
            imagenRes = imagenRes,
            imagenUri = imagenUri,
            likesBase = 1,
            recetaOficialId = recetaAsociadaId,
            comentarios = emptyList()
        )
        _publicacionesComunidad.update { listOf(nuevaPublicacion) + it }
        return nuevoId
    }

    fun actualizarFotoBorrador(uri: Uri?, fallbackRes: Int) {
        _borradorReceta.update {
            it.copy(imagenUri = uri, imagenRes = fallbackRes)
        }
    }

    fun actualizarVideoBorrador(uri: Uri?) {
        _borradorReceta.update {
            it.copy(videoUri = uri, videoRes = 0)
        }
    }

    fun limpiarVideoBorrador() {
        _borradorReceta.update {
            it.copy(videoUri = null, videoRes = 0)
        }
    }

    fun eliminarMiReceta(recetaId: Int) {
        _recetasOficiales.update { lista ->
            lista.filterNot { it.id == recetaId && it.esPropiaUsuario }
        }
        _recetasGuardadasIds.update { it - recetaId }
        _carpetasUsuario.update { carpetas ->
            carpetas.map { c ->
                c.copy(recetasOficialesIds = c.recetasOficialesIds - recetaId)
            }
        }
    }

    private fun coloresPorCategoria(categoria: String): Pair<Color, Color> {
        return when (categoria) {
            "Chocolatería" -> Color(0xFFF3E5DE) to Color(0xFFC37B58)
            "Bollería" -> Color(0xFFD6F0EB) to Color(0xFF258674)
            "Gelato" -> Color(0xFFE1EEF8) to Color(0xFF507FA6)
            else -> Color(0xFFF2ECDC) to Color(0xFF8E793C)
        }
    }

    // =========================================================================
    // ACCIONES DE CURSOS ("APRENDE")
    // =========================================================================
    fun seleccionarCursoDetalle(curso: CursoGastronomico?) {
        _cursoSeleccionadoDetalle.value = curso
    }

    fun toggleSuscripcionPro() {
        _suscritoAcademyPro.update { !it }
    }

    fun inscribirCurso(cursoId: Int) {
        _cursosCompradosIds.update { it + cursoId }
        _progresoCursos.update { mapa ->
            val actual = mapa[cursoId] ?: 0f
            if (actual == 0f) mapa + (cursoId to 0.15f) else mapa
        }
    }

    fun comprarCurso(cursoId: Int) {
        _cursosCompradosIds.update { it + cursoId }
        _progresoCursos.update { mapa ->
            val actual = mapa[cursoId] ?: 0f
            if (actual == 0f) mapa + (cursoId to 0.15f) else mapa
        }
    }

    fun toggleLeccionCompletada(cursoId: Int, leccionId: Int) {
        _cursos.update { lista ->
            lista.map { c ->
                if (c.id == cursoId) {
                    val nuevasLecciones = c.lecciones.map { l ->
                        if (l.id == leccionId) l.copy(completada = !l.completada) else l
                    }
                    val completadas = nuevasLecciones.count { it.completada }
                    val nuevoProgreso = if (nuevasLecciones.isNotEmpty()) {
                        completadas.toFloat() / nuevasLecciones.size
                    } else 0f
                    _progresoCursos.update { it + (cursoId to nuevoProgreso) }
                    c.copy(lecciones = nuevasLecciones)
                } else c
            }
        }
        _cursoSeleccionadoDetalle.value?.let { actual ->
            if (actual.id == cursoId) {
                _cursoSeleccionadoDetalle.value = _cursos.value.find { it.id == cursoId }
            }
        }
    }

    fun inscribirOAvanzarCurso(cursoId: Int) {
        _cursosCompradosIds.update { it + cursoId }
        _progresoCursos.update { mapa ->
            val actual = mapa[cursoId] ?: 0f
            val siguiente = (actual + 0.25f).coerceAtMost(1f)
            mapa + (cursoId to if (actual == 0f) 0.25f else siguiente)
        }
    }

    // =========================================================================
    // DATOS INICIALES: RECETAS EN DESCUBRE (OFICIALES MARTINI + USUARIOS)
    // =========================================================================
    private fun crearRecetasIniciales(): List<RecetaOficial> {
        return listOf(
            RecetaOficial(
                id = 100,
                titulo = "Torta Bianca de Chocolate Blanco Ariba & Frutos Rojos",
                subtitulo = "Entremet sedoso de chocolate blanco marfil con crema montada Decor Up y acidez fresca de frambuesas y moras.",
                categoria = "Pastelería",
                tiempo = "40'",
                nivel = "Intermedio",
                porciones = "10 porciones",
                imagenRes = R.drawable.receta_ricette_hero,
                autor = "Master Martini Oficial",
                esOficialMartini = true,
                esRecetaDelDia = true,
                ingredientes = listOf(
                    "320 g de Ariba Chocolate Blanco en discos",
                    "450 ml de crema vegetal Decor Up refrigerada (4°C)",
                    "120 ml de leche entera o bebida OraSì Almendra",
                    "8 g de gelatina sin sabor hidratada",
                    "180 g de bizcocho joconde de almendra para la base",
                    "150 g de frambuesas y moras frescas para coronar"
                ),
                pasos = listOf(
                    "Fundir los discos de Ariba Chocolate Blanco a baño maría suave (máximo 42°C) hasta lograr una textura lisa.",
                    "Calentar los 120 ml de leche, disolver la gelatina hidratada y verter en tres tandas sobre el chocolate blanco emulsionando desde el centro.",
                    "Semimontar la crema Decor Up fría a velocidad media hasta punto cinta firme.",
                    "Cuando la ganache baje a 30°C, incorporar Decor Up con movimientos envolventes.",
                    "Verter sobre el aro con base de bizcocho, alisar, refrigerar 4 horas y decorar con rosetas finas y frutos rojos frescos."
                ),
                productosAsociadosIds = listOf("ariba-choc", "decor-up"),
                colorCategoriaBg = Color(0xFFF2ECDC),
                colorCategoriaTexto = Color(0xFF8E793C)
            ),
            RecetaOficial(
                id = 1,
                titulo = "Copa Gianduja & Mousse Caravella",
                subtitulo = "Postre en capas de crema de avellanas tostadas, biscuit húmedo de cacao y mousse aireada.",
                categoria = "Pastelería",
                tiempo = "38'",
                nivel = "Fácil",
                porciones = "6 copas",
                imagenRes = R.drawable.receta_pasteleria,
                autor = "Master Martini Oficial",
                esOficialMartini = true,
                ingredientes = listOf(
                    "220 g de Caravella Crema Avellana y Cacao",
                    "300 ml de Decor Up Crema UHT bien fría",
                    "100 g de Ariba Leche 36% para el aro y virutas",
                    "90 g de crumble crujiente de cacao y avellanas europeas"
                ),
                pasos = listOf(
                    "Disponer una base de crumble de cacao en el fondo de cada copa de doble pared.",
                    "Aligerar la mitad de Caravella Avellana con un tercio de Decor Up batida para formar la capa de gianduja aireada.",
                    "Montar el resto de Decor Up e intercalar capas limpias con manga pastelera.",
                    "Coronar con avellanas tostadas enteras y aro fino de chocolate Ariba templado."
                ),
                productosAsociadosIds = listOf("caravella-cream", "decor-up", "ariba-choc"),
                colorCategoriaBg = Color(0xFFF2ECDC),
                colorCategoriaTexto = Color(0xFF8E793C)
            ),
            RecetaOficial(
                id = 6,
                titulo = "Kuchen de Nuez Tradicional",
                subtitulo = "Es mucho más fácil de lo que parece, lleva ingredientes fáciles de conseguir y es perfecto para ese antojo dulce de invierno.",
                categoria = "Pastelería",
                tiempo = "50'",
                nivel = "Fácil",
                porciones = "8 porciones",
                imagenRes = R.drawable.receta_foodservice,
                videoRes = R.raw.video_don_saco_roto_1,
                autor = "Don Saco Roto",
                esOficialMartini = false,
                esPropiaUsuario = false,
                haceCuanto = "3d.",
                ingredientes = listOf(
                    "250 g de nueces cortadas en trozos pequeños (sin que queden excesivamente molidas)",
                    "1 taza de leche entera (o leche evaporada)",
                    "60 g de mantequilla (o margarina Maxime)",
                    "120 g de azúcar granulada",
                    "1 cucharadita de extracto de vainilla",
                    "100 g de mantequilla blanda para la masa",
                    "80 g de azúcar flor para la masa",
                    "1 chorrito de leche para ligar la masa",
                    "1 huevo entero",
                    "250 g de harina sin polvos de hornear",
                    "Mantequilla y harina extra para enmantequillar el molde"
                ),
                pasos = listOf(
                    "Cortar las nueces en trozos pequeños con cuchillo, cuidando que no queden excesivamente molidas para mantener la textura crujiente.",
                    "En una olla a fuego medio, colocar la leche con la mantequilla, el azúcar, la vainilla y las nueces picadas. Cocinar revolviendo constantemente hasta que el azúcar se disuelva y la mezcla espese en una crema acaramelada.",
                    "Para la masa, en un bol mezclar mantequilla con azúcar, un chorrito de leche y el huevo. Incorporar la harina poco a poco hasta formar una masa suave que no se pegue en las manos.",
                    "Enmantequillar y enharinar un molde de tarta. Extender la masa de forma pareja con las manos cubriendo el fondo y los bordes.",
                    "Verter sobre la masa la mezcla tibia de nueces caramelizadas de manera uniforme.",
                    "Llevar a hornear a 180°C durante 25 a 30 minutos hasta que la masa esté dorada y el relleno caramelizado.",
                    "Dejar enfriar completamente antes de cortar para que el relleno tome consistencia perfecta."
                ),
                productosAsociadosIds = listOf("decor-up", "caravella-cream"),
                colorCategoriaBg = Color(0xFFF2ECDC),
                colorCategoriaTexto = Color(0xFF8E793C)
            ),
            RecetaOficial(
                id = 7,
                titulo = "Huevos Marinados para Ramen",
                subtitulo = "Seguramente has visto el huevo que lleva el ramen. Esto es comer arroz con huevo pero al siguiente nivel, con una yema cremosa y una marinada llena de sabor.",
                categoria = "Salados",
                tiempo = "15' (24h reposo)",
                nivel = "Fácil",
                porciones = "6 unidades",
                imagenRes = R.drawable.receta_foodservice,
                videoRes = R.raw.video_don_saco_roto_2,
                autor = "Don Saco Roto",
                esOficialMartini = false,
                esPropiaUsuario = false,
                haceCuanto = "1d.",
                ingredientes = listOf(
                    "6 huevos frescos (a temperatura ambiente)",
                    "Agua hirviendo a borbotones para la cocción",
                    "Agua con abundante hielo para el choque térmico",
                    "1 taza de salsa de soya",
                    "1/2 taza de agua purificada",
                    "2 a 3 cucharadas de azúcar",
                    "1 cucharada de vinagre de manzana o de arroz",
                    "2 tallos de cebollín picados finamente",
                    "1 chalota (o cebolla morada) picada fina",
                    "2 a 3 dientes de ajo picados finos",
                    "2 cucharadas de semillas de sésamo tostadas",
                    "Arroz blanco caliente al vapor para servir"
                ),
                pasos = listOf(
                    "Hervir abundante agua en una olla. Introducir con cuidado los huevos y cocinar durante exactamente 6 minutos para lograr una yema cremosa y perfecta (¡6 minutos es el punto exacto!).",
                    "Retirar de inmediato con espumadera y traspasar a un bol con agua muy fría y abundante hielo para detener la cocción por completo.",
                    "Dejar enfriar 5 minutos y pelar con cuidado bajo el chorro de agua fría.",
                    "En un recipiente hondo mezclar el cebollín, la chalota, el ajo picado, las semillas de sésamo, la salsa de soya, el vinagre, el agua y el azúcar, revolviendo hasta disolver.",
                    "Introducir con cuidado los huevos pelados en el recipiente, asegurando que queden completamente cubiertos por la marinada.",
                    "Tapar herméticamente y refrigerar durante 24 horas para que absorban todo el sabor, color y aroma.",
                    "Servir sobre un cuenco de arroz blanco caliente, partir el huevo al medio para apreciar la yema cremosa y bañar con un par de cucharadas de la marinada y sésamo."
                ),
                productosAsociadosIds = emptyList(),
                colorCategoriaBg = Color(0xFFF3E5DE),
                colorCategoriaTexto = Color(0xFFC37B58)
            ),
            RecetaOficial(
                id = 8,
                titulo = "Torta Hojarasca Caravella & Merengue",
                subtitulo = "Capas finas y crocantes de hojarasca casera rellenas con abundantes capas de Caravella Avellana y merengue suizo.",
                categoria = "Pastelería",
                tiempo = "55'",
                nivel = "Intermedio",
                porciones = "12 porciones",
                imagenRes = R.drawable.receta_pasteleria,
                autor = "Don Saco Roto",
                esOficialMartini = false,
                esPropiaUsuario = false,
                haceCuanto = "5d.",
                ingredientes = listOf(
                    "12 discos de hojarasca tradicional chilena",
                    "350 g de Caravella Crema Avellana y Cacao",
                    "200 g de manjar artesanal",
                    "4 claras con 200 g de azúcar para merengue suizo"
                ),
                pasos = listOf(
                    "Hornear los discos de hojarasca hasta que queden dorados y secos.",
                    "Intercalar capas generosas de Caravella Avellana y manjar artesanal.",
                    "Cubrir con merengue suizo flameado con soplete pastelero."
                ),
                productosAsociadosIds = listOf("caravella-cream"),
                colorCategoriaBg = Color(0xFFF2ECDC),
                colorCategoriaTexto = Color(0xFF8E793C)
            ),
            RecetaOficial(
                id = 2,
                titulo = "Praliné Espejo de Maracuyá & Ariba",
                subtitulo = "Bombones de corte geométrico con camisa fina y relleno fluido tropical.",
                categoria = "Chocolatería",
                tiempo = "53'",
                nivel = "Avanzado",
                porciones = "24 bombones",
                imagenRes = R.drawable.receta_cioccolato,
                autor = "Master Martini Oficial",
                esOficialMartini = true,
                ingredientes = listOf(
                    "400 g de Ariba Blanco y Ariba Amargo 72% para encamisado",
                    "140 g de pulpa natural de maracuyá reducida",
                    "200 g de chocolate blanco Ariba para la ganache",
                    "35 g de manteca de cacao coloreada natural"
                ),
                pasos = listOf(
                    "Pulir los moldes de policarbonato con algodón y aplicar un velo fino de manteca de cacao a 29°C.",
                    "Templar Ariba (curva 45°C → 27°C → 31°C), encamisar los moldes y escurrir el exceso para lograr casquillos delgados.",
                    "Emulsionar la reducción tibia de maracuyá con el chocolate blanco hasta obtener una ganache brillante y dosificar a 28°C.",
                    "Dejar cristalizar 2 horas, sellar la base con chocolate templado y desmoldar con golpe seco."
                ),
                productosAsociadosIds = listOf("ariba-choc"),
                colorCategoriaBg = Color(0xFFF3E5DE),
                colorCategoriaTexto = Color(0xFFC37B58)
            ),
            RecetaOficial(
                id = 3,
                titulo = "Danish Roll con Margarina Maxime",
                subtitulo = "Espirales hojaldradas de fermentación lenta rellenas de crema pastelera y pasas al ron.",
                categoria = "Bollería",
                tiempo = "45'",
                nivel = "Intermedio",
                porciones = "12 piezas",
                imagenRes = R.drawable.receta_frozen,
                autor = "Master Martini Oficial",
                esOficialMartini = true,
                ingredientes = listOf(
                    "500 g de harina panadera de fuerza",
                    "250 g de Margarina Maxime Hojaldre en placa",
                    "200 ml de leche fría, 60 g de azúcar y 12 g de levadura fresca",
                    "250 g de crema pastelera vainilla y 90 g de pasas rubias y negras"
                ),
                pasos = listOf(
                    "Amasar el empaste base, enfriar a 4°C durante 40 minutos y encerrar la placa de Margarina Maxime a la misma plasticidad.",
                    "Realizar 3 pliegues simples respetando reposos en frío de 25 minutos entre cada vuelta.",
                    "Estirar a 3.5 mm, extender una capa fina de crema pastelera y distribuir las pasas hidratadas.",
                    "Enrollar, cortar discos de 2.5 cm, fermentar a 26°C y hornear a 190°C por 16-18 minutos hasta dorar."
                ),
                productosAsociadosIds = listOf("maxime-butter"),
                colorCategoriaBg = Color(0xFFD6F0EB),
                colorCategoriaTexto = Color(0xFF258674)
            ),
            RecetaOficial(
                id = 9,
                titulo = "Croissant Bicolor Relleno de Caravella Avellana",
                subtitulo = "Laminado crujiente en 3 pliegues simples con corazón cremoso inyectado post-horneo.",
                categoria = "Bollería",
                tiempo = "55'",
                nivel = "Avanzado",
                porciones = "10 croissants",
                imagenRes = R.drawable.receta_frozen,
                autor = "Valentina Soto",
                esOficialMartini = false,
                esPropiaUsuario = true,
                haceCuanto = "5d.",
                ingredientes = listOf(
                    "500 g de harina de fuerza + 25 g de cacao para la capa bicolor",
                    "250 g de Margarina Maxime Hojaldre",
                    "180 g de Caravella Avellana y Cacao para relleno"
                ),
                pasos = listOf(
                    "Laminar la masa fría con Margarina Maxime en 3 vueltas simples y adherir la lámina fina de masa al cacao.",
                    "Cortar triángulos de 9x24 cm, enrollar, fermentar 2 h a 26°C y hornear a 190°C.",
                    "Entibiar e inyectar Caravella Avellana desde la base con boquilla fina."
                ),
                productosAsociadosIds = listOf("maxime-butter", "caravella-cream"),
                colorCategoriaBg = Color(0xFFD6F0EB),
                colorCategoriaTexto = Color(0xFF258674)
            ),
            RecetaOficial(
                id = 4,
                titulo = "Gelato Artesanal Pistacho & Mango OraSì",
                subtitulo = "Textura cremosa 100% vegetal equilibrada con bebida OraSì y fruta natural.",
                categoria = "Gelato",
                tiempo = "35'",
                nivel = "Intermedio",
                porciones = "1.5 litros",
                imagenRes = R.drawable.receta_gelato,
                autor = "Master Martini Oficial",
                esOficialMartini = true,
                ingredientes = listOf(
                    "650 ml de OraSì Bebida Vegetal Almendra o Avena",
                    "180 g de pulpa de mango maduro / pasta pura de pistacho",
                    "140 g de sacarosa, 45 g de dextrosa y 5 g de neutro para gelato"
                ),
                pasos = listOf(
                    "Mezclar en seco los azúcares con el estabilizante neutro para evitar grumos.",
                    "Integrar con OraSì en tibio (65°C), enfriar rápidamente a 4°C y madurar la mezcla por 4 horas.",
                    "Sumar la fruta o pasta pura con mixer de inmersión y mantecar hasta alcanzar el punto de espátula.",
                    "Abatir a -18°C durante 15 minutos antes de exhibir en vitrina."
                ),
                productosAsociadosIds = listOf("orasi-barista"),
                colorCategoriaBg = Color(0xFFE1EEF8),
                colorCategoriaTexto = Color(0xFF507FA6)
            ),
            RecetaOficial(
                id = 5,
                titulo = "Tartaleta Ganache Montada Caravella",
                subtitulo = "Masa sablée de cacao crujiente coronada con espiral de ganache aireada y avellanas.",
                categoria = "Pastelería",
                tiempo = "42'",
                nivel = "Fácil",
                porciones = "8 tartaletas",
                imagenRes = R.drawable.receta_foodservice,
                autor = "Master Martini Oficial",
                esOficialMartini = true,
                ingredientes = listOf(
                    "8 bases de masa sablée de cacao horneadas en blanco",
                    "240 g de Caravella Cacao & Avellana",
                    "260 ml de crema vegetal Decor Up fría",
                    "Discos Ariba 72% picados para textura interior"
                ),
                pasos = listOf(
                    "Impermeabilizar el interior de las tartaletas frías con una pincelada fina de cobertura fundida.",
                    "Disponer un corazón crujiente de Ariba 72% picado en la base.",
                    "Batir Decor Up junto a Caravella hasta lograr picos sedosos y escudillar con boquilla rizada.",
                    "Terminar con avellanas caramelizadas y cacao amargo espolvoreado."
                ),
                productosAsociadosIds = listOf("caravella-cream", "decor-up", "ariba-choc"),
                colorCategoriaBg = Color(0xFFF2ECDC),
                colorCategoriaTexto = Color(0xFF8E793C)
            )
        )
    }

    // =========================================================================
    // DATOS INICIALES: FORO DE COMUNIDAD (CONSULTAS, DATOS DE OFICIO Y "ASÍ ME QUEDÓ")
    // =========================================================================
    private fun crearPublicacionesIniciales(): List<PublicacionComunidad> {
        return listOf(
            PublicacionComunidad(
                id = 201,
                tipoPublicacion = "Comunidad",
                autor = "Valentina Soto",
                rolAutor = "Pastelería Artesanal • Providencia",
                esOficialMartini = false,
                esPropiaUsuario = true,
                haceCuanto = "Hace 1 h",
                titulo = "¿A qué temperatura montan Decor Up en verano cuando el obrador pasa de 26°C?",
                descripcion = "En días calurosos noto que si bato a velocidad alta me cuesta mantener los picos definidos al manguear rosetas finas. ¿Ustedes enfrían también el bowl antes o trabajan solo a velocidad media?",
                productosUtilizados = emptyList(),
                imagenRes = 0,
                likesBase = 24,
                repostsBase = 5,
                recetaOficialId = null,
                comentarios = listOf(
                    ComentarioComunidad(
                        id = 1,
                        autor = "Chef Matteo Rossi",
                        haceCuanto = "Hace 38 min",
                        texto = "¡Hola Valentina! Clave refrigerar el envase mínimo 12 h entre 4°C y 6°C y batir a velocidad media (nunca máxima) con bowl frío: incorpora microburbujas parejas que resisten el calor de sala.",
                        esOficialMartini = true
                    ),
                    ComentarioComunidad(
                        id = 2,
                        autor = "Cristóbal Vera",
                        haceCuanto = "Hace 15 min",
                        texto = "Tal cual dice el Chef: velocidad media y llenar solo hasta 1/4 de la cubeta de la batidora cambia todo el rendimiento."
                    )
                )
            ),
            PublicacionComunidad(
                id = 202,
                tipoPublicacion = "Comunidad",
                autor = "Cristóbal Vera",
                rolAutor = "Banquetería & Eventos • Concepción",
                esOficialMartini = false,
                esPropiaUsuario = false,
                haceCuanto = "Hace 4 h",
                titulo = "Así quedaron las mini Tortas Bianca en formato individual para matrimonio",
                descripcion = "Adaptamos la receta oficial a aros de 7 cm para 120 invitados. El corte salió súper limpio y aguantaron 3 horas montadas en mesa dulce sin soltar nada de suero.",
                productosUtilizados = emptyList(),
                imagenRes = R.drawable.receta_ricette_hero,
                likesBase = 42,
                repostsBase = 8,
                recetaOficialId = 100,
                comentarios = listOf(
                    ComentarioComunidad(
                        id = 1,
                        autor = "Don Saco Roto",
                        haceCuanto = "Hace 2 h",
                        texto = "¡Se ven impecables! ¿Cuánto tiempo de abatidor les diste antes de desmoldar los aros?"
                    )
                )
            ),
            PublicacionComunidad(
                id = 203,
                tipoPublicacion = "Comunidad",
                autor = "Chef Matteo Rossi",
                rolAutor = "Technical Chef Master Martini",
                esOficialMartini = true,
                esPropiaUsuario = false,
                haceCuanto = "Ayer",
                titulo = "Tip rápido para evitar vetas grises al desmoldar bombones con Ariba 72%",
                descripcion = "Antes de encamisar, verifiquen con termómetro infrarrojo que el molde de policarbonato esté entre 20°C y 22°C. Si el molde está muy frío (<16°C), el choque térmico opaca el brillo espejo aunque el templado esté perfecto.",
                productosUtilizados = emptyList(),
                imagenRes = 0,
                likesBase = 53,
                repostsBase = 16,
                recetaOficialId = 2,
                comentarios = listOf(
                    ComentarioComunidad(
                        id = 1,
                        autor = "Diego Muñoz",
                        haceCuanto = "Hace 18 h",
                        texto = "Tremendo dato, en invierno en Valparaíso me pasaba siempre hasta que empecé a templar levemente los moldes con pistola de calor suave."
                    )
                )
            ),
            PublicacionComunidad(
                id = 204,
                tipoPublicacion = "Comunidad",
                autor = "Diego Muñoz",
                rolAutor = "Barista & Gelatiere • Valparaíso",
                esOficialMartini = false,
                esPropiaUsuario = false,
                haceCuanto = "Hace 2 d",
                titulo = "Prueba de vitrina: Gelato 100% plant-based con base OraSì",
                descripcion = "Hicimos la tanda de fin de semana siguiendo el balance de azúcares de la receta y la espátula quedó igual de sedosa que una base láctea tradicional.",
                productosUtilizados = emptyList(),
                imagenRes = R.drawable.receta_gelato,
                likesBase = 31,
                repostsBase = 4,
                recetaOficialId = 4,
                comentarios = emptyList()
            ),
            PublicacionComunidad(
                id = 205,
                tipoPublicacion = "Comunidad",
                autor = "Don Saco Roto",
                rolAutor = "Maestro Pastelero • Santiago",
                esOficialMartini = false,
                esPropiaUsuario = false,
                haceCuanto = "Hace 3 d",
                titulo = "Kuchen de Nuez Tradicional",
                descripcion = "Es mucho más fácil de lo que parece y con pocos ingredientes. La clave: cocinar las nueces con leche, azúcar y mantequilla hasta espesar, y una masa rápida que no se pega en las manos.",
                productosUtilizados = emptyList(),
                imagenRes = R.drawable.receta_foodservice,
                videoRes = R.raw.video_don_saco_roto_1,
                likesBase = 34,
                repostsBase = 12,
                recetaOficialId = 6,
                comentarios = listOf(
                    ComentarioComunidad(
                        id = 1,
                        autor = "Sofía Valenzuela",
                        haceCuanto = "Hace 2 d",
                        texto = "¡Quedó espectacular Don Saco Roto! El caramelo de nuez tiene la textura ideal, bien húmedo."
                    ),
                    ComentarioComunidad(
                        id = 2,
                        autor = "Chef Matteo Rossi",
                        haceCuanto = "Hace 1 d",
                        texto = "Excelente técnica maestro. Agregar un toque de sal marina al caramelo realza aún más las nueces.",
                        esOficialMartini = true
                    )
                )
            ),
            PublicacionComunidad(
                id = 207,
                tipoPublicacion = "Comunidad",
                autor = "Don Saco Roto",
                rolAutor = "Creador Gastronómico • Santiago",
                esOficialMartini = false,
                esPropiaUsuario = false,
                haceCuanto = "Hace 1 d",
                titulo = "Huevos Marinados para Ramen",
                descripcion = "Seguramente has visto el huevo que lleva el ramen. Esto es comer arroz con huevo pero al siguiente nivel: 6 minutos exactos en agua hirviendo, choque con hielo y 24 horas en salsa de soya con cebollín, chalota y sésamo.",
                productosUtilizados = emptyList(),
                imagenRes = R.drawable.receta_foodservice,
                videoRes = R.raw.video_don_saco_roto_2,
                likesBase = 46,
                repostsBase = 18,
                recetaOficialId = 7,
                comentarios = listOf(
                    ComentarioComunidad(
                        id = 1,
                        autor = "Valentina Soto",
                        haceCuanto = "Hace 1 d",
                        texto = "¡Quedan atómicos! El marinado de 24 horas les da un sabor increíble al arroz con huevo."
                    ),
                    ComentarioComunidad(
                        id = 2,
                        autor = "Chef Matteo Rossi",
                        haceCuanto = "Hace 16 h",
                        texto = "Tremendo dato el de los 6 minutos exactos de cocción, la yema queda cremosa y perfecta.",
                        esOficialMartini = true
                    )
                )
            ),
            PublicacionComunidad(
                id = 206,
                tipoPublicacion = "Comunidad",
                autor = "Mesa Técnica Master Martini",
                rolAutor = "Technical Team",
                esOficialMartini = true,
                esPropiaUsuario = false,
                haceCuanto = "Oficial",
                titulo = "Consultas: Copa Gianduja & Mousse Caravella",
                descripcion = "Hilo de consultas técnicas sobre montado con Decor Up.",
                productosUtilizados = emptyList(),
                imagenRes = R.drawable.receta_pasteleria,
                likesBase = 19,
                repostsBase = 6,
                recetaOficialId = 1,
                comentarios = listOf(
                    ComentarioComunidad(
                        id = 1,
                        autor = "Don Saco Roto",
                        haceCuanto = "Hace 4 h",
                        texto = "¿Qué boquilla recomiendan para escudillar la mousse en copa cónica?"
                    ),
                    ComentarioComunidad(
                        id = 2,
                        autor = "Chef Matteo Rossi",
                        haceCuanto = "Hace 2 h",
                        texto = "Una boquilla rizada de 12 mm permite mantener la definición de los surcos sin calentar la crema con la mano.",
                        esOficialMartini = true
                    )
                )
            )
        )
    }

    private fun crearCarpetasIniciales(): List<CarpetaRecetas> {
        return listOf(
            CarpetaRecetas(
                id = 1,
                nombre = "Vitrina Pastelería",
                recetasOficialesIds = listOf(100, 1, 5),
                esPrivada = false,
                esGrupo = false,
                actualizadoHace = "2d."
            ),
            CarpetaRecetas(
                id = 2,
                nombre = "Ideas con Ariba",
                recetasOficialesIds = listOf(2, 100, 6),
                esPrivada = false,
                esGrupo = true,
                actualizadoHace = "4d."
            ),
            CarpetaRecetas(
                id = 3,
                nombre = "Bollería & Masas",
                recetasOficialesIds = listOf(3, 7, 5),
                esPrivada = true,
                esGrupo = false,
                actualizadoHace = "1sem."
            ),
            CarpetaRecetas(
                id = 4,
                nombre = "Heladería & Café",
                recetasOficialesIds = listOf(4, 1, 100),
                esPrivada = false,
                esGrupo = false,
                actualizadoHace = "2sem."
            )
        )
    }

    private fun crearCursosIniciales(): List<CursoGastronomico> {
        return listOf(
            CursoGastronomico(
                id = 1,
                moduloNumero = 1,
                titulo = "Bombonería de Autor & Templado Preciso con Ariba",
                nivel = "Intermedio",
                chef = "Chef Matteo Rossi",
                duracion = "3h 20m",
                leccionesCount = 6,
                insumoClave = "Ariba Cioccolato Puro",
                resumen = "Domina curvas de cristalización térmica, encamisados ultradelgados y ganaches estables de larga vida útil.",
                descripcionDetallada = "Aprende de manera práctica junto al Maestro Chocolatero Matteo Rossi los secretos del templado profesional en mármol y por siembra utilizando coberturas de cacao fino Ariba Master Martini. Conocerás el manejo exacto de temperaturas (fusión, descenso y remonte) para asegurar brillo espejo, contracción limpia y una textura crocante irresistible.",
                imagenRes = R.drawable.curso_bombones_finitos,
                esGratuito = true,
                precioTexto = "Gratis",
                rating = 5.0f,
                reviewsCount = 2431,
                siglasBadge = "Ar",
                colorBadgeHex = 0xFF5C3828L,
                colorFondoCardHex = 0xFFFBF4F0L,
                descuentoBadge = "Popular",
                lecciones = listOf(
                    LeccionCurso(id = 101, numero = 1, titulo = "Introducción al Cacao y Variedades Ariba", duracionTexto = "14 mins", completada = true),
                    LeccionCurso(id = 102, numero = 2, titulo = "Curva Térmica y Cristalización Beta Forma V", duracionTexto = "22 mins", completada = true),
                    LeccionCurso(id = 103, numero = 3, titulo = "Encamisado de Moldes de Policarbonato", duracionTexto = "18 mins", completada = false),
                    LeccionCurso(id = 104, numero = 4, titulo = "Ganache de Maracuyá y Ariba Blanco 31%", duracionTexto = "25 mins", completada = false),
                    LeccionCurso(id = 105, numero = 5, titulo = "Sellado de Fondo, Cristalización y Desmolde", duracionTexto = "20 mins", completada = false),
                    LeccionCurso(id = 106, numero = 6, titulo = "Conservación, Empaque y Control de Humedad", duracionTexto = "16 mins", completada = false)
                )
            ),
            CursoGastronomico(
                id = 2,
                moduloNumero = 2,
                titulo = "Entremets Modernos & Montado Estable con Decor Up",
                nivel = "Básico",
                chef = "Chef Sofía Lombardi",
                duracion = "2h 45m",
                leccionesCount = 5,
                insumoClave = "Decor Up UHT & Caravella",
                resumen = "Técnicas de batido al punto de nieve, estabilidad de vitrina a temperatura ambiente y mangas de precisión.",
                descripcionDetallada = "Descubre las propiedades de alto rendimiento de la crema vegetal Decor Up de Master Martini. Este curso te enseñará la relación óptima de temperatura (4-6°C), tiempo de batido y técnicas para cuadruplicar el volumen sin riesgo de separación o cuarteado en tortas de vitrina.",
                imagenRes = R.drawable.receta_ricette_hero,
                esGratuito = true,
                precioTexto = "Gratis",
                rating = 4.8f,
                reviewsCount = 1820,
                siglasBadge = "Du",
                colorBadgeHex = 0xFF00695CL,
                colorFondoCardHex = 0xFFF0FDF8L,
                descuentoBadge = "Recomendado",
                lecciones = listOf(
                    LeccionCurso(id = 201, numero = 1, titulo = "Temperatura Óptima y Reposo de Decor Up", duracionTexto = "12 mins", completada = false),
                    LeccionCurso(id = 202, numero = 2, titulo = "Técnicas de Batido y Control de Volumen", duracionTexto = "19 mins", completada = false),
                    LeccionCurso(id = 203, numero = 3, titulo = "Mangueado de Bordes, Rosetas y Copetes", duracionTexto = "24 mins", completada = false),
                    LeccionCurso(id = 204, numero = 4, titulo = "Inclusión de Pulpa Ácida y Chocolate Caravella", duracionTexto = "28 mins", completada = false),
                    LeccionCurso(id = 205, numero = 5, titulo = "Comportamiento en Vitrina Refrigerada por 72h", duracionTexto = "15 mins", completada = false)
                )
            ),
            CursoGastronomico(
                id = 3,
                moduloNumero = 3,
                titulo = "Diplomado Bollería Laminada Europea con Margarina Maxime",
                nivel = "Avanzado",
                chef = "Chef Jean-Luc Ferrand",
                duracion = "4h 15m",
                leccionesCount = 6,
                insumoClave = "Línea Maxime Laminación",
                resumen = "Control de plasticidad, pliegues franceses, fermentación controlada y alveolado de medialunas y croissants.",
                descripcionDetallada = "Especialización avanzada en masas hojaldradas y bollería vienesa de alta gama. Profundizaremos en la plasticidad y punto de fusión constante de las margarinas técnicas Maxime de Grupo Unigrà, logrando capas crujientes y un alveolado panal de abeja digno de concurso internacional.",
                imagenRes = R.drawable.curso_croissants_maxime,
                esGratuito = false,
                precioTexto = "$19.990 CLP",
                precioOriginal = "$26.990 CLP",
                rating = 4.9f,
                reviewsCount = 3120,
                siglasBadge = "Mx",
                colorBadgeHex = 0xFFE65100L,
                colorFondoCardHex = 0xFFFFF8E1L,
                descuentoBadge = "Bestseller",
                lecciones = listOf(
                    LeccionCurso(id = 301, numero = 1, titulo = "Selección de Harinas de Fuerza W y Amasado", duracionTexto = "25 mins", completada = false),
                    LeccionCurso(id = 302, numero = 2, titulo = "Empaste y Temperatura de Margarina Maxime", duracionTexto = "30 mins", completada = false),
                    LeccionCurso(id = 303, numero = 3, titulo = "Pliegues Simples y Dobles: El Descanso Térmico", duracionTexto = "35 mins", completada = false),
                    LeccionCurso(id = 304, numero = 4, titulo = "Corte, Estirado y Laminado de Croissants", duracionTexto = "28 mins", completada = false),
                    LeccionCurso(id = 305, numero = 5, titulo = "Fermentación Lenta y Pincelado de Brillo", duracionTexto = "22 mins", completada = false),
                    LeccionCurso(id = 306, numero = 6, titulo = "Horneo a Convección y Análisis de Alveolos", duracionTexto = "20 mins", completada = false)
                )
            ),
            CursoGastronomico(
                id = 4,
                moduloNumero = 4,
                titulo = "Gelatería Italiana & Formulación Plant-Based OraSì",
                nivel = "Intermedio",
                chef = "Maestro Paolo Conti",
                duracion = "2h 30m",
                leccionesCount = 4,
                insumoClave = "OraSì Barista Avena & Soja",
                resumen = "Balanceo de azúcares, sólidos no grasos (PAC/POD) y texturas sedosas 100% libres de lactosa.",
                descripcionDetallada = "Aprende a formular y producir auténtico gelato italiano artesanal sin lácteos utilizando las bebidas vegetales de barista OraSì. Aprenderás el cálculo de balanceo entre poder anticongelante y dulzor relativo, manteniendo elasticidad y cremosidad a -14°C.",
                imagenRes = R.drawable.curso_gelato_orasi,
                esGratuito = true,
                precioTexto = "Gratis",
                rating = 4.9f,
                reviewsCount = 950,
                siglasBadge = "Or",
                colorBadgeHex = 0xFF2E7D32L,
                colorFondoCardHex = 0xFFF1F8E9L,
                descuentoBadge = "Gratis",
                lecciones = listOf(
                    LeccionCurso(id = 401, numero = 1, titulo = "Conceptos de PAC, POD y Grasas Vegetales", duracionTexto = "20 mins", completada = false),
                    LeccionCurso(id = 402, numero = 2, titulo = "Emulsión en Caliente con OraSì Barista", duracionTexto = "25 mins", completada = false),
                    LeccionCurso(id = 403, numero = 3, titulo = "Mantencación y Manteñado a -14°C", duracionTexto = "22 mins", completada = false),
                    LeccionCurso(id = 404, numero = 4, titulo = "Servicio y Espatulado en Vitrina", duracionTexto = "18 mins", completada = false)
                )
            ),
            CursoGastronomico(
                id = 5,
                moduloNumero = 5,
                titulo = "Masterclass Chocolatería 3D & Escultura Ariba 72%",
                nivel = "Avanzado Pro",
                chef = "Chef Matteo Rossi",
                duracion = "5h 30m",
                leccionesCount = 7,
                insumoClave = "Ariba Fondente 72% & Manteca",
                resumen = "Arquitectura estructural en chocolate, uso de nitrógeno líquido, aerógrafo y modelado de piezas gigantes.",
                descripcionDetallada = "La masterclass insignia de Master Martini Academy para profesionales de alta repostería. Desarrolla habilidades escultóricas con chocolate oscuro puro Ariba Fondente 72%, aplicando técnicas de encastre mecánico, pegado criogénico y terminaciones aterciopeladas con pistola y manteca de cacao pura.",
                imagenRes = R.drawable.curso_chocolateria_nueva,
                esGratuito = false,
                precioTexto = "$24.990 CLP",
                precioOriginal = "$35.990 CLP",
                rating = 5.0f,
                reviewsCount = 4890,
                siglasBadge = "Ar",
                colorBadgeHex = 0xFF4A148CL,
                colorFondoCardHex = 0xFFF3E5F5L,
                descuentoBadge = "30% OFF",
                lecciones = listOf(
                    LeccionCurso(id = 501, numero = 1, titulo = "Propiedades Mecánicas del Cacao al 72%", duracionTexto = "28 mins", completada = false),
                    LeccionCurso(id = 502, numero = 2, titulo = "Moldeado de Vigas y Cilindros de Apoyo", duracionTexto = "32 mins", completada = false),
                    LeccionCurso(id = 503, numero = 3, titulo = "Pegado Estructural y Choque Criogénico", duracionTexto = "35 mins", completada = false),
                    LeccionCurso(id = 504, numero = 4, titulo = "Modelado Orgánico de Pétalos y Hojas Curvas", duracionTexto = "40 mins", completada = false),
                    LeccionCurso(id = 505, numero = 5, titulo = "Acabado Terciopelo con Aerógrafo y Manteca", duracionTexto = "30 mins", completada = false),
                    LeccionCurso(id = 506, numero = 6, titulo = "Equilibrio, Centro de Gravedad y Montaje", duracionTexto = "45 mins", completada = false),
                    LeccionCurso(id = 507, numero = 7, titulo = "Embalaje Antichoque y Transporte a Eventos", duracionTexto = "25 mins", completada = false)
                )
            ),
            CursoGastronomico(
                id = 6,
                moduloNumero = 6,
                titulo = "Alta Pastelería & Glaseados Espejo con Mirall",
                nivel = "Intermedio - Pro",
                chef = "Chef Sofía Lombardi",
                duracion = "3h 40m",
                leccionesCount = 5,
                insumoClave = "Glaseados Mirall Frutti & Cioccolato",
                resumen = "Coberturas espejo de reflejo perfecto, curvas de vertido a 32°C y combinación con bizcochos dacquoise.",
                descripcionDetallada = "Lleva tus vitrinas al siguiente nivel con la línea de glaseados profesionales Mirall Master Martini. Domina el vertido uniforme sin burbujas sobre mousses ultracongeladas a -18°C y el efecto telaraña marmoleado.",
                imagenRes = R.drawable.curso_glaseado_mirall,
                esGratuito = false,
                precioTexto = "$21.990 CLP",
                precioOriginal = "$27.990 CLP",
                rating = 4.9f,
                reviewsCount = 1430,
                siglasBadge = "Mi",
                colorBadgeHex = 0xFFAD1457L,
                colorFondoCardHex = 0xFFFCE4ECL,
                descuentoBadge = "Nuevo",
                lecciones = listOf(
                    LeccionCurso(id = 601, numero = 1, titulo = "Congelación y Desmolde de Entremets Silikomart", duracionTexto = "20 mins", completada = false),
                    LeccionCurso(id = 602, numero = 2, titulo = "Termodinámica y Fluidez del Glaseado Mirall", duracionTexto = "26 mins", completada = false),
                    LeccionCurso(id = 603, numero = 3, titulo = "Técnica de Baño de Cascada Bicolor", duracionTexto = "32 mins", completada = false),
                    LeccionCurso(id = 604, numero = 4, titulo = "Decoración con Crispy Balls y Pan de Oro", duracionTexto = "22 mins", completada = false),
                    LeccionCurso(id = 605, numero = 5, titulo = "Corte Preciso y Porcionado en Mini Gateaux", duracionTexto = "18 mins", completada = false)
                )
            ),
            CursoGastronomico(
                id = 7,
                moduloNumero = 7,
                titulo = "Cremas Finas de Relleno & Mousses Bravo Crem",
                nivel = "Básico - Intermedio",
                chef = "Maestro Paolo Conti",
                duracion = "2h 55m",
                leccionesCount = 5,
                insumoClave = "Bravo Crem & Pastas Concentradas",
                resumen = "Mousses ligeras, rellenos de larga duración sin decantación y estabilidad en postres de vaso.",
                descripcionDetallada = "Aprende a formular rellenos suaves y untuosos con Bravo Crem. Su fórmula de alta tolerancia al calor y acidez la convierte en el aliado ideal para repostería masiva y cafeterías de alto flujo.",
                imagenRes = R.drawable.curso_mousse_bravocrem,
                esGratuito = false,
                precioTexto = "$18.990 CLP",
                precioOriginal = "$24.990 CLP",
                rating = 4.8f,
                reviewsCount = 890,
                siglasBadge = "Br",
                colorBadgeHex = 0xFF1565C0L,
                colorFondoCardHex = 0xFFE3F2FDL,
                descuentoBadge = "Membresía Pro",
                lecciones = listOf(
                    LeccionCurso(id = 701, numero = 1, titulo = "Características de Emulsión de Bravo Crem", duracionTexto = "16 mins", completada = false),
                    LeccionCurso(id = 702, numero = 2, titulo = "Integración con Chocolate Tibio y Gelificantes", duracionTexto = "24 mins", completada = false),
                    LeccionCurso(id = 703, numero = 3, titulo = "Mousse Ligera de Café y Avellana Europea", duracionTexto = "28 mins", completada = false),
                    LeccionCurso(id = 704, numero = 4, titulo = "Montaje de Postres en Vaso y Vasitos Finger Food", duracionTexto = "22 mins", completada = false),
                    LeccionCurso(id = 705, numero = 5, titulo = "Envasado Hermético y Vida Útil en Frío", duracionTexto = "15 mins", completada = false)
                )
            )
        )
    }
}
