package com.example.appmastermartini_grupo9.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declaramos el DataStore a nivel de contexto con el nombre 'preferencias_usuario' (Guía 12)
val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

/**
 * Gestor de persistencia local mediante DataStore Preferences (Guía 12).
 * Guarda y recupera de forma reactiva el estado del "Modo Especial / Catálogo Premium"
 * y el estado de sesión activa del usuario.
 */
class EstadoDataStore(private val context: Context) {

    // Clave para guardar el valor booleano (Modo Especial / Tema)
    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo_activado")

    // Claves para persistir la sesión de usuario
    private val SESION_INICIADA = booleanPreferencesKey("sesion_iniciada")
    private val NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
    private val CORREO_USUARIO = stringPreferencesKey("correo_usuario")
    private val HANDLE_USUARIO = stringPreferencesKey("handle_usuario")

    // Claves de Accesibilidad para Adultos Mayores
    private val ESCALA_FUENTE = floatPreferencesKey("escala_fuente_accesibilidad")
    private val ONBOARDING_FUENTE_MOSTRADO = booleanPreferencesKey("onboarding_fuente_mostrado")

    // Función suspendida para persistir el estado en disco
    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    // Función para obtener el estado como un Flow reactivo
    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO]
        }
    }

    // Guarda o finaliza la sesión activa del usuario en DataStore
    suspend fun guardarSesion(iniciada: Boolean, nombre: String = "", correo: String = "", handle: String = "") {
        context.dataStore.edit { preferencias ->
            preferencias[SESION_INICIADA] = iniciada
            if (iniciada) {
                if (nombre.isNotBlank()) preferencias[NOMBRE_USUARIO] = nombre
                if (correo.isNotBlank()) preferencias[CORREO_USUARIO] = correo
                if (handle.isNotBlank()) preferencias[HANDLE_USUARIO] = handle
            } else {
                preferencias.remove(NOMBRE_USUARIO)
                preferencias.remove(CORREO_USUARIO)
                preferencias.remove(HANDLE_USUARIO)
            }
        }
    }

    // Retorna si la sesión ha sido iniciada previamente
    fun obtenerSesionIniciada(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[SESION_INICIADA]
        }
    }

    // Retorna los datos básicos del usuario autenticado guardados
    fun obtenerUsuarioGuardado(): Flow<Triple<String, String, String>> {
        return context.dataStore.data.map { preferencias ->
            Triple(
                preferencias[NOMBRE_USUARIO] ?: "",
                preferencias[CORREO_USUARIO] ?: "",
                preferencias[HANDLE_USUARIO] ?: ""
            )
        }
    }

    // Guarda el factor de escala de fuente preferido por el usuario
    suspend fun guardarEscalaFuente(escala: Float) {
        context.dataStore.edit { preferencias ->
            preferencias[ESCALA_FUENTE] = escala
        }
    }

    // Retorna el factor de escala de fuente (1.0f por defecto si no se ha configurado)
    fun obtenerEscalaFuente(): Flow<Float> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESCALA_FUENTE] ?: 1.0f
        }
    }

    // Marca si el onboarding inicial de selección de tamaño de fuente ya fue completado
    suspend fun guardarOnboardingFuenteMostrado(mostrado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ONBOARDING_FUENTE_MOSTRADO] = mostrado
        }
    }

    // Retorna si ya se mostró la pantalla de onboarding de fuente
    fun obtenerOnboardingFuenteMostrado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ONBOARDING_FUENTE_MOSTRADO]
        }
    }
}
