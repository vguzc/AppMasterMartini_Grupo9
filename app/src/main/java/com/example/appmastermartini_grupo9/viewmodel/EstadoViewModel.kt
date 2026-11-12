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

    // Estado reactivo de persistencia de sesión de usuario en DataStore
    private val _sesionIniciada = MutableStateFlow<Boolean?>(null)
    val sesionIniciada: StateFlow<Boolean?> = _sesionIniciada.asStateFlow()

    // Estado reactivo de escala de tamaño de fuente (Accesibilidad Adultos Mayores)
    private val _escalaFuente = MutableStateFlow(1.0f)
    val escalaFuente: StateFlow<Float> = _escalaFuente.asStateFlow()

    // Estado para saber si se debe mostrar la pantalla inicial de selección de tamaño de letra
    private val _onboardingFuenteMostrado = MutableStateFlow<Boolean?>(null)
    val onboardingFuenteMostrado: StateFlow<Boolean?> = _onboardingFuenteMostrado.asStateFlow()

    val usuarioGuardado = estadoDataStore.obtenerUsuarioGuardado()

    init {
        // Al iniciar el ViewModel, cargamos el estado persistido desde DataStore
        cargarEstado()
        cargarSesion()
        cargarAccesibilidadFuente()
    }

    fun cargarSesion() {
        viewModelScope.launch {
            estadoDataStore.obtenerSesionIniciada().collect { iniciada ->
                _sesionIniciada.value = iniciada ?: false
            }
        }
    }

    fun guardarSesion(iniciada: Boolean, nombre: String = "", correo: String = "", handle: String = "") {
        viewModelScope.launch {
            estadoDataStore.guardarSesion(iniciada, nombre, correo, handle)
            _sesionIniciada.value = iniciada
        }
    }

    fun cargarEstado() {
        viewModelScope.launch {
            estadoDataStore.obtenerEstado().collect { valorGuardado ->
                _activo.value = valorGuardado ?: false
            }
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

    private fun cargarAccesibilidadFuente() {
        viewModelScope.launch {
            estadoDataStore.obtenerEscalaFuente().collect { escala ->
                _escalaFuente.value = escala
            }
        }
        viewModelScope.launch {
            estadoDataStore.obtenerOnboardingFuenteMostrado().collect { mostrado ->
                _onboardingFuenteMostrado.value = mostrado ?: false
            }
        }
    }

    fun guardarEscalaFuente(escala: Float) {
        viewModelScope.launch {
            estadoDataStore.guardarEscalaFuente(escala)
            _escalaFuente.value = escala
        }
    }

    fun marcarOnboardingFuenteMostrado() {
        viewModelScope.launch {
            estadoDataStore.guardarOnboardingFuenteMostrado(true)
            _onboardingFuenteMostrado.value = true
        }
    }
}
