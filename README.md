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
  * **Pantalla 1 (Home):** Integra `ModalNavigationDrawer` con menú lateral (`ModalDrawerSheet`) y `TopAppBar` con botón hamburguesa.
  * **Pantalla 2 (Profile / Catálogo):** Integra `NavigationBar` (BottomBar) y catálogo de insumos de Master Martini (*Ariba, Caravella, Decor Up, OraSì*).
  * **Pantalla 3 (Settings / Contacto):** Información corporativa de Master Martini Chile (Av. Américo Vespucio 1385, Quilicura, Santiago), teléfono y botones de navegación.
* **Conexión en MainActivity:** `NavHost` controlado de forma reactiva con `LaunchedEffect` escuchando el `SharedFlow` del `MainViewModel`.
* **Commit:** `"Navegación y menú generado"`.

### 🔹 Guía 11: Formularios, validaciones y pasado de información
* **Modelado del estado:** Clases de datos `UsuarioUiState` y `UsuarioErrores` en `model/`.
* **Lógica del ViewModel:** `UsuarioViewModel.kt` usando `MutableStateFlow` para reactividad en tiempo real y función `validarFormulario()`.
* **Pantalla de Registro:** `RegistroScreen.kt` con componentes de Material 3 (`OutlinedTextField`, `PasswordVisualTransformation`, `Checkbox`, `Button`, y manejo de errores con `isError` y `supportingText`).
* **Pantalla de Resumen:** `ResumenScreen.kt` que observa el mismo `UsuarioViewModel` compartido mediante `collectAsState()`, visualizando los datos sin necesidad de pasarlos por parámetros en la ruta.
* **Navegación:** `AppNavigation.kt` y registro en el NavHost central.
* **Rama y Commit:** `feature/registro-formulario` con mensaje `"Formulario y resumen funcional"`.

### 🔹 Guía 12: Animaciones y Estados
* **Persistencia local:** `EstadoDataStore.kt` utilizando `androidx.datastore:datastore-preferences` (`preferencias_usuario`) para guardar de forma reactiva el estado de activación ("Modo Profesional Master Martini").
* **Lógica del ViewModel:** `EstadoViewModel.kt` con `MutableStateFlow`, simulación de carga con retardo (`delay`), alternancia de estado y mensaje animado temporal.
* **Feedback interactivo:** `PantallaAnimaciones.kt` con:
  * `CircularProgressIndicator` durante la carga inicial del estado.
  * `animateColorAsState` para transicionar suavemente el color del botón según el estado activo/inactivo.
  * `derivedStateOf` para derivar reactivamente el texto del botón ("Activar" / "Desactivar").
  * `AnimatedVisibility` para mostrar con animación el mensaje de confirmación cuando se guarda el estado.
* **Commit:** `"Estado y animaciones"`.

### 🔹 Guía 13: Integrando Recurso Nativo Cámara y Galería
* **Permisos & FileProvider:** Permiso de cámara declarado en `AndroidManifest.xml` y configuración de `FileProvider` con `res/xml/file_paths.xml` para compartir URIs seguros.
* **ViewModel:** `PerfilViewModel.kt` con `MutableStateFlow<Uri?>` para mantener sincronizado el URI de la fotografía.
* **Componente Inteligente:** `ImagenInteligente.kt` dentro de `ui/components/` que dibuja la foto de perfil en forma circular (`clip(CircleShape)` y borde) mediante Coil (`AsyncImage`), y en caso de ausencia muestra un icono por defecto (`Icons.Default.Person`).
* **Pantalla Perfil con Cámara:** `PerfilFotoScreen.kt` con lanzadores `rememberLauncherForActivityResult`:
  * Galería: `ActivityResultContracts.GetContent()`
  * Cámara: `ActivityResultContracts.TakePicture()` con URI temporal seguro.
  * Control de errores para evitar cierres inesperados al cancelar la captura.
* **Rama y Commit:** `feature/imagen-inteligente` con mensaje `"Funcionalidad cámara y galería integrada"`.

---

## 🚀 Historial de Commits y Ramas en GitHub

| Rama | Hash | Mensaje de Commit | Guía / Hito |
| :--- | :--- | :--- | :--- |
| `feature/home-screen` | `4f240b5` | `"Pantalla HomeScreen con estructura Scaffold"` | **Guía 8** |
| `master` | `e53ef06` | `"feat: diseño adaptativo con Window Size Classes (Compact, Medium, Expanded)"` | **Guía 9** |
| `master` | `7ea0af8` | `"Navegación y menú generado"` | **Guía 10** |
| `feature/registro-formulario` | `6716fc3` | `"Formulario y resumen funcional"` | **Guía 11** |
| `master` | `0d695ec` | `"Estado y animaciones"` | **Guía 12** |
| `feature/imagen-inteligente` | `463f99f` | `"Funcionalidad cámara y galería integrada"` | **Guía 13** |
| `master` | *reciente* | `"docs: actualizar README con documentación de Guías 11, 12 y 13"` | **Documentación Completa** |

---

## 🏗️ Arquitectura del Proyecto (MVVM Estándar 2025)

El código fuente en `app/src/main/java/com/example/appmastermartini_grupo9/` sigue una rigurosa separación de responsabilidades:

* `data/`: Persistencia local con DataStore Preferences (`EstadoDataStore.kt`).
* `model/`: Entidades de dominio, estado y errores (`ProductItem.kt`, `UsuarioUiState.kt`, `UsuarioErrores.kt`).
* `repository/`: Proveedor de datos del catálogo de Master Martini (`ProductRepository.kt`).
* `viewmodel/`: `MainViewModel.kt`, `UsuarioViewModel.kt`, `EstadoViewModel.kt`, `PerfilViewModel.kt`.
* `navigation/`: Rutas tipadas (`Screen.kt`), eventos (`NavigationEvent.kt`) y flujos (`AppNavigation.kt`).
* `ui/`:
  * `components/`: Componentes reutilizables como `ImagenInteligente.kt`.
  * `screens/`: `HomeScreen.kt`, `HomeScreenAdaptive.kt`, `ProfileScreen.kt`, `SettingsScreen.kt`, `RegistroScreen.kt`, `ResumenScreen.kt`, `PantallaAnimaciones.kt`, `PerfilFotoScreen.kt`.
  * `utils/`: `WindowSizeUtils.kt` para diseño adaptativo.
  * `theme/`: Paleta oficial Master Martini (`MartiniNavy`, `MartiniGold`, `MartiniCream`), tipografía y tema Material 3.

---

## 👥 Integrantes - Grupo 9
* *Guillermo Manríquez*
* *Apolo Vallejos*
* *Vicente Guzmán*
