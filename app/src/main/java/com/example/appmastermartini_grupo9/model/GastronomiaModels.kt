package com.example.appmastermartini_grupo9.model

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Modelo unificado de Receta en "Descubre":
 * Conviven las recetas oficiales de Master Martini (con sello verificado)
 * y las recetas subidas por los usuarios de la comunidad.
 */
data class RecetaOficial(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val categoria: String,
    val tiempo: String,
    val nivel: String,
    val porciones: String,
    @param:DrawableRes val imagenRes: Int,
    val imagenUri: Uri? = null,
    val videoRes: Int = 0,
    val videoUri: Uri? = null,
    val autor: String = "Master Martini Oficial",
    val esOficialMartini: Boolean = true,
    val esPropiaUsuario: Boolean = false,
    val esRecetaDelDia: Boolean = false,
    val haceCuanto: String = "Oficial",
    val ingredientes: List<String>,
    val pasos: List<String>,
    val productosAsociadosIds: List<String>,
    val colorCategoriaBg: Color,
    val colorCategoriaTexto: Color
)

/**
 * Comentario o respuesta dentro de un hilo del Foro en Comunidad (Lógica Threads).
 */
data class ComentarioComunidad(
    val id: Int,
    val autor: String,
    val haceCuanto: String,
    val texto: String,
    val esOficialMartini: Boolean = false,
    val likesCount: Int = 0,
    val parentComentarioId: Int? = null
)

/**
 * Publicación del Foro / Muro de Pasteleros en Comunidad (Estética Master Martini + Lógica Threads):
 * Consultas técnicas, datos de oficio y muestras de "Así me quedó"
 * enlazadas a recetas, productos, con hilos de respuestas conectadas y reposts.
 */
data class PublicacionComunidad(
    val id: Int,
    val tipoPublicacion: String = "Consulta técnica", // "Consulta técnica", "Dato de oficio", "Así me quedó"
    val autor: String,
    val rolAutor: String,
    val esOficialMartini: Boolean = false,
    val esPropiaUsuario: Boolean = false,
    val haceCuanto: String,
    val titulo: String,
    val descripcion: String,
    val productosUtilizados: List<String> = emptyList(),
    @param:DrawableRes val imagenRes: Int = 0,
    val imagenUri: Uri? = null,
    val videoRes: Int = 0,
    val videoUri: Uri? = null,
    val likesBase: Int = 1,
    val repostsBase: Int = 0,
    val recetaOficialId: Int? = null,
    val comentarios: List<ComentarioComunidad> = emptyList()
)

/**
 * Lección individual dentro de un curso de formación gastronómica.
 */
data class LeccionCurso(
    val id: Int,
    val numero: Int,
    val titulo: String,
    val duracionTexto: String,
    val completada: Boolean = false
)

/**
 * Curso o taller de formación gastronómica en la sección Aprende (Cursos).
 */
data class CursoGastronomico(
    val id: Int,
    val moduloNumero: Int,
    val titulo: String,
    val nivel: String,
    val chef: String,
    val duracion: String,
    val leccionesCount: Int,
    val insumoClave: String,
    val resumen: String,
    val descripcionDetallada: String = resumen,
    @param:DrawableRes val imagenRes: Int,
    val esGratuito: Boolean = true,
    val precioTexto: String = "Gratis",
    val precioOriginal: String? = null,
    val rating: Float = 4.9f,
    val reviewsCount: Int = 1240,
    val siglasBadge: String = "MM",
    val colorBadgeHex: Long = 0xFF5C3828L,
    val colorFondoCardHex: Long = 0xFFFFF7F2L,
    val descuentoBadge: String? = null,
    val lecciones: List<LeccionCurso> = emptyList()
)

/**
 * Colección personalizada creada por el usuario para organizar recetas guardadas.
 */
data class CarpetaRecetas(
    val id: Int,
    val nombre: String,
    val recetasOficialesIds: List<Int> = emptyList(),
    val postsComunidadIds: List<Int> = emptyList(),
    val esPrivada: Boolean = false,
    val esGrupo: Boolean = false,
    val actualizadoHace: String = "Ahora"
)

/**
 * Borrador persistido en memoria mientras el usuario crea una receta para Descubre.
 */
data class BorradorReceta(
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "Pastelería",
    val tiempo: String = "35'",
    val ingredientes: String = "",
    val pasos: String = "",
    val productosSeleccionados: Set<String> = setOf("Ariba Chocolate Puro"),
    @param:DrawableRes val imagenRes: Int = 0,
    val imagenUri: Uri? = null,
    val videoRes: Int = 0,
    val videoUri: Uri? = null
)
