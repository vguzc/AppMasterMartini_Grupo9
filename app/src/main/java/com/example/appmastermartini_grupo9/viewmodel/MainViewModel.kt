package com.example.appmastermartini_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmastermartini_grupo9.navigation.NavigationEvent
import com.example.appmastermartini_grupo9.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel principal (Guía 10 y patrón MVVM).
 * Centraliza y emite los eventos de navegación mediante un SharedFlow.
 */
class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    // Expone el canal como un Flow de solo lectura para ser observado por la UI en MainActivity
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    /**
     * Emite evento de navegación hacia la ruta deseada
     */
    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    /**
     * Emite evento para volver a la pantalla anterior
     */
    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    /**
     * Emite evento para navegar hacia arriba
     */
    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}
