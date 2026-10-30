package com.example.appmastermartini_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appmastermartini_grupo9.model.UsuarioErrores
import com.example.appmastermartini_grupo9.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para gestionar el estado reactivo del formulario de usuario (Guía 11).
 * Implementa validaciones en tiempo real y persistencia del estado en memoria
 * para ser compartido entre RegistroScreen y ResumenScreen.
 */
class UsuarioViewModel : ViewModel() {

    // Estado interno mutable
    private val _estado = MutableStateFlow(UsuarioUiState())
    // Estado expuesto inmutable para la UI
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    // Actualiza el campo nombre y limpia su error
    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(nombre = valor, errores = it.errores.copy(nombre = null))
        }
    }

    // Actualiza el campo handle (nombre de usuario) y limpia su error
    fun onHandleChange(valor: String) {
        val limpio = valor.trim().removePrefix("@").replace(" ", "").lowercase()
        _estado.update {
            it.copy(handle = limpio, errores = it.errores.copy(handle = null))
        }
    }

    // Actualiza el campo correo y limpia su error
    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(correo = valor, errores = it.errores.copy(correo = null))
        }
    }

    // Actualiza el campo clave y limpia su error
    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(clave = valor, errores = it.errores.copy(clave = null))
        }
    }

    // Actualiza el campo confirmar clave
    fun onConfirmarClaveChange(valor: String) {
        _estado.update {
            it.copy(confirmarClave = valor)
        }
    }

    // Actualiza el campo teléfono
    fun onTelefonoChange(valor: String) {
        _estado.update {
            it.copy(telefono = valor)
        }
    }

    // Actualiza el campo dirección y limpia su error
    fun onDireccionChange(valor: String) {
        _estado.update {
            it.copy(direccion = valor, errores = it.errores.copy(direccion = null))
        }
    }

    // Actualiza el checkbox de aceptación de términos
    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(aceptaTerminos = valor)
        }
    }

    // Validación global del formulario
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo inválido" else null,
            clave = if (estadoActual.clave.length < 6) "Debe tener al menos 6 caracteres" else null,
            direccion = if (estadoActual.direccion.isBlank()) "Campo obligatorio" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion
        ).isNotEmpty() || !estadoActual.aceptaTerminos

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}
