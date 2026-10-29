package com.example.appmastermartini_grupo9.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declaramos el DataStore a nivel de contexto con el nombre 'preferencias_usuario' (Guía 12)
val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

/**
 * Gestor de persistencia local mediante DataStore Preferences (Guía 12).
 * Guarda y recupera de forma reactiva el estado del "Modo Especial / Catálogo Premium".
 */
class EstadoDataStore(private val context: Context) {

    // Clave para guardar el valor booleano
    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo_activado")

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
}
