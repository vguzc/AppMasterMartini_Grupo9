package com.example.appmastermartini_grupo9.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmastermartini_grupo9.data.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel que extiende de AndroidViewModel para acceder al contexto y DataStore (Guía 12).
 * Gestiona la simulación de carga, alternancia de estado y mensajes animados.
 */
class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    private val estadoDataStore = EstadoDataStore(application)

    // Estado que representa si está "activado" o no (null representa estado de carga)
    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo.asStateFlow()

    // Estado para mostrar u ocultar el mensaje animado con AnimatedVisibility
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje.asStateFlow()

    init {
        // Al iniciar el ViewModel, cargamos el estado persistido desde DataStore
        cargarEstado()
    }

    fun cargarEstado() {
        viewModelScope.launch {
            // Simula demora para mostrar loader (CircularProgressIndicator)
            delay(1500)
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            // Alternamos el valor actual
            val nuevoValor = !(_activo.value ?: false)

            // Guardamos persistentemente en DataStore
            estadoDataStore.guardarEstado(nuevoValor)

            // Actualizamos el flujo reactivo
            _activo.value = nuevoValor

            // Mostramos el mensaje visual animado
            _mostrarMensaje.value = true

            // Ocultamos después de 2 segundos
            delay(2000)
            _mostrarMensaje.value = false
        }
    }
}
