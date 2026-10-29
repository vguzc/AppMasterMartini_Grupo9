package com.example.appmastermartini_grupo9.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para gestionar el recurso nativo de imagen (Cámara y Galería) (Guía 13).
 * Mantiene de manera reactiva el URI de la imagen seleccionada o capturada.
 */
class PerfilViewModel : ViewModel() {

    // Variable para guardar el URI de la imagen seleccionada o capturada
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    // Función para actualizar la imagen (desde galería o cámara)
    fun actualizarImagen(uri: Uri?) {
        if (uri != null) {
            _imagenUri.value = uri
        }
    }

    // Función para limpiar la foto de perfil si se desea
    fun limpiarImagen() {
        _imagenUri.value = null
    }
}
