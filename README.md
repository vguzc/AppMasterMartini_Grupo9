# AppMasterMartini - Grupo 9

Aplicación móvil desarrollada en Android con **Jetpack Compose (Material 3)** aplicando la arquitectura **MVVM (Model-View-ViewModel)** para la empresa **Master Martini Chile** ([mastermartini.cl](https://mastermartini.cl/)), filial oficial del Grupo Unigrà especializada en insumos vegetales y soluciones para chocolatería, pastelería y panadería profesional.

---

## 📌 Gestión del Proyecto y Tareas (Trello)

El seguimiento y la planificación del desarrollo del proyecto se gestionan en el siguiente tablero de Trello:

🔗 **[Tablero de Trello - Master Martini](https://trello.com/b/LjtchmiV/master-martini)**

### Listas del Tablero:
* 📋 **Por hacer (To Do):** Tareas planificadas para los próximos sprints.
* ⏳ **En curso (Doing):** Tareas actualmente en desarrollo.
* ✅ **Finalizado (Done):** Tareas completadas y verificadas.

---

## 📱 Guías Desarrolladas e Implementación Técnica

### 🔹 Guía 8: Construcción visual de pantalla base con Jetpack Compose
* **Estructura visual:** Uso de `Scaffold`, `TopAppBar`, `Column`, `Row`, `Text`, `Button` e `Image`.
* **Identidad de marca:** Inclusión del recurso vectorial `logo.xml` (`R.drawable.logo`) con el emblema e identidad de Master Martini.
* **Estilizado y buenas prácticas:** Uso de `Modifier` (`padding`, `fillMaxSize`, `verticalArrangement = Arrangement.spacedBy`) y paleta de colores corporativa mediante `MaterialTheme`.
* **Rama y Commit:** `feature/home-screen` con mensaje `"Pantalla HomeScreen con estructura Scaffold"`.

### 🔹 Guía 9: Adaptabilidad del diseño (Window Size Classes)
* **Cálculo de pantalla:** Módulo utilitario `WindowSizeUtils.kt` con `obtenerWindowSizeClass()`.
* **Variaciones de pantalla:**
  * `HomeScreenCompacta`: Optimizada verticalmente para smartphones.
  * `HomeScreenMediana`: Layout balanceado para dispositivos plegables o tablets pequeñas.
  * `HomeScreenExpandida`: Layout de dos columnas con panel maestro y detalle para tablets grandes y monitores.
* **Componente selector:** `HomeScreenAdaptive` que mediante `when (windowSizeClass.widthSizeClass)` selecciona dinámicamente la interfaz correcta.
* **Previsualización:** Previews dedicadas `@Preview` para cada tamaño (`Compact`, `Medium`, `Expanded`).

### 🔹 Guía 10: Navegación y estructura visual en aplicaciones (MVVM)
* **Rutas tipo-safe:** `Screen.kt` utilizando `sealed class` (`Home`, `Profile`, `Settings`, `Detail`).
* **Eventos desacoplados:** `NavigationEvent.kt` (`NavigateTo`, `PopBackStack`, `NavigateUp`).
* **Lógica en ViewModel:** `MainViewModel.kt` emitiendo eventos de navegación mediante `SharedFlow<NavigationEvent>`.
* **Componentes de navegación:**
  * **Pantalla 1 (Home):** Integra `ModalNavigationDrawer` con menú lateral (`ModalDrawerSheet`) y `TopAppBar` con botón hamburguesa para navegar hacia el catálogo o configuración.
  * **Pantalla 2 (Profile / Catálogo):** Integra `NavigationBar` (BottomBar) y listado de líneas de productos profesionales (Ariba, Caravella, Decor Up, OraSì).
  * **Pantalla 3 (Settings / Contacto):** Información corporativa de Master Martini Chile (Av. Américo Vespucio 1385, Quilicura, Santiago), teléfono y botones de navegación.
* **Conexión en MainActivity:** `NavHost` controlado de forma reactiva con `LaunchedEffect` escuchando el `SharedFlow` del `MainViewModel`.
* **Commit:** `"Navegación y menú generado"`.

---

## 🏗️ Arquitectura del Proyecto (MVVM Estándar 2025)

El código fuente en `app/src/main/java/com/example/appmastermartini_grupo9/` sigue una rigurosa separación de responsabilidades:

* `model/`: Entidades de datos y DTOs (`ProductItem`).
* `repository/`: Proveedor de datos desacoplado del catálogo de Master Martini (`ProductRepository`).
* `viewmodel/`: `MainViewModel` para gestión del estado y eventos de navegación.
* `navigation/`: Definición segura de rutas (`Screen`) y eventos de navegación (`NavigationEvent`).
* `ui/`:
  * `screens/`: `HomeScreen.kt`, `HomeScreenAdaptive.kt`, `ProfileScreen.kt`, `SettingsScreen.kt`.
  * `utils/`: `WindowSizeUtils.kt` para cálculo de `WindowSizeClass`.
  * `theme/`: Paleta de colores oficial de Master Martini (`MartiniNavy`, `MartiniGold`, etc.), tipografía y tema Material 3.

---

## 👥 Integrantes - Grupo 9
* *Guillermo Manríquez*
* *Apolo Vallejos*
* *Vicente Guzmán*
