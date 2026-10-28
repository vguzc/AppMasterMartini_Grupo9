package com.example.appmastermartini_grupo9.navigation

/**
 * Representa los distintos tipos de eventos de navegación (Guía 10).
 * Permite desacoplar los eventos de navegación desde el ViewModel hacia la vista.
 */
sealed class NavigationEvent {
    /**
     * Evento para navegar a un destino específico.
     * @param route Destino tipado al que navegar.
     * @param popUpToRoute Ruta de destino hasta la cual se hace pop en la pila.
     * @param inclusive Si es true, la ruta popUpToRoute también se elimina de la pila.
     * @param singleTop Si es true, evita múltiples copias del mismo destino en la parte superior.
     */
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    /**
     * Evento para volver a la pantalla anterior en la pila de navegación.
     */
    data object PopBackStack : NavigationEvent()

    /**
     * Evento para navegar hacia arriba en la jerarquía de la aplicación.
     */
    data object NavigateUp : NavigationEvent()
}
