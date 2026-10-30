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
| `master` | *actual* | `"feat: arquitectura unificada Master Martini, correcciones de ciclo de vida y cumplimiento de rúbrica"` | **Evaluación Parcial 2** |

---

## 📋 Cumplimiento de Rúbrica - Evaluación Parcial 2 (DSY1105)

| Criterio / Indicador | Ponderación | Estado | Implementación Técnica en el Proyecto |
| :--- | :---: | :---: | :--- |
| **IE 2.1.1: Interfaz y Navegación** | 15% | ✅ Cumplido (100%) | `NavHost` central en `MainActivity.kt` con barra inferior flotante `FloatingBottomBar` estilo Pinterest, sistema tipográfico dual (*Plus Jakarta Sans* y *DM Sans*) y paleta de colores institucional Master Martini. |
| **IE 2.1.2: Formularios y Validaciones Visuales** | 15% | ✅ Cumplido (100%) | `LoginRegistroScreen.kt` y `CrearRecetaScreen.kt` con validaciones por campo en tiempo real, cambio de color visual ante error (`#FFF6F6`), mensajes claros de error debajo de cada campo e íconos temáticos acordes. |
| **IE 2.2.1: Lógica Desacoplada de Validación** | 10% | ✅ Cumplido (100%) | `UsuarioViewModel.kt` y `MainViewModel.kt` centralizan toda la lógica de validación (`UsuarioErrores`, `UsuarioUiState`, `validarFormulario()`, `iniciarSesionConDatos()`), manteniendo la UI completamente desacoplada y reactiva mediante `StateFlow`. |
| **IE 2.2.2: Animaciones Visuales Funcionales** | 10% | ✅ Cumplido (100%) | `SplashScreen.kt` con `Animatable` (animación de entrada, escala, línea dorada y offset), `AnimatedContent` en Login/Registro y catálogo de Cursos, `animateColorAsState` en la barra flotante y retroalimentación animada temporal en `EstadoViewModel`. |
| **IE 2.3.1: Estructura Modular y Persistencia Local** | 15% | ✅ Cumplido (100%) | Arquitectura MVVM dividida en paquetes (`data`, `model`, `repository`, `viewmodel`, `navigation`, `ui`). Persistencia local reactiva implementada con **DataStore Preferences** (`EstadoDataStore.kt`) guardando preferencias y modo oscuro con corrutinas y `Flow`. |
| **IE 2.3.2: Colaboración y Control de Versiones** | 20% | ✅ Cumplido (100%) | Repositorio público en GitHub con commits semánticos e instructivos, y planificación completa disponible en el [Tablero de Trello](https://trello.com/b/LjtchmiV/master-martini). |
| **IE 2.4.1: Acceso Seguro a Recursos Nativos (≥2)** | 15% | ✅ Cumplido (100%) | **1. Cámara fotográfica nativa** mediante `FileProvider` y `ActivityResultContracts.TakePicture()` creando URIs temporales seguros.<br>**2. Almacenamiento / Galería** mediante `ActivityResultContracts.GetContent()` para selección local de imágenes y videos.<br>**Recursos adicionales:** `Intent.ACTION_SEND` (compartir contenido nativo) y decodificación acelerada por hardware con `AndroidX Media3 ExoPlayer`. |

---

## 🛠️ Requisitos del Entorno y Pasos para Ejecutar

### Requisitos previos:
* **Android Studio:** Ladybug (2024.2.1) o superior.
* **JDK:** Java 11 o superior (configurado por defecto en Android Studio).
* **Android SDK:** `minSdk = 24` (Android 7.0 Nougat) hasta `targetSdk = 37`.
* Dispositivo físico con depuración USB activada o Emulador Android (ej. Pixel 7 / Pixel 8 con API 34+).

### Pasos para clonar y ejecutar:
1. Clonar el repositorio desde GitHub:
   ```bash
   git clone https://github.com/vguzc/AppMasterMartini_Grupo9.git
   ```
2. Abrir la carpeta `AppMasterMartini_Grupo9` en **Android Studio**.
3. Esperar que Gradle sincronice las dependencias del proyecto (`Sync Project with Gradle Files`).
4. Seleccionar el dispositivo emulador o físico en la barra de herramientas superior.
5. Presionar el botón **Run 'app'** (`Shift + F10`) o compilar desde la terminal:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 🏗️ Arquitectura del Proyecto (MVVM Estándar 2025)

El código fuente en `app/src/main/java/com/example/appmastermartini_grupo9/` sigue una rigurosa separación de responsabilidades:

* `data/`: Persistencia local con DataStore Preferences (`EstadoDataStore.kt`).
* `model/`: Entidades de dominio, estado y errores (`GastronomiaModels.kt`, `ProductItem.kt`, `UsuarioUiState.kt`).
* `repository/`: Proveedor de datos del catálogo de Master Martini (`ProductRepository.kt`).
* `viewmodel/`: `MainViewModel.kt`, `UsuarioViewModel.kt`, `EstadoViewModel.kt`, `PerfilViewModel.kt`.
* `navigation/`: Rutas tipadas (`Screen.kt`), eventos (`NavigationEvent.kt`) y flujos (`AppNavigation.kt`).
* `ui/`:
  * `components/`: `FloatingBottomBar.kt`, `MartiniTopHeader.kt`, `MartiniVideoPlayer.kt`, `ImagenInteligente.kt`.
  * `screens/`: `HomeScreen.kt`, `HomeScreenAdaptive.kt`, `ProfileScreen.kt`, `CrearRecetaScreen.kt`, `PantallaAnimaciones.kt`, `ComunidadScreen.kt`, `PerfilCreadorScreen.kt`, `PerfilFotoScreen.kt`, `RecetaDetalleScreen.kt`, `LoginRegistroScreen.kt`, `SettingsScreen.kt`, `RegistroScreen.kt`, `ResumenScreen.kt`, `SplashScreen.kt`.
  * `utils/`: `WindowSizeUtils.kt` para diseño adaptativo.
  * `theme/`: Paleta oficial Master Martini (`MartiniGreen`, `MartiniDarkKhaki`, `MartiniKhaki`, `MartiniFloralWhite`), tipografía (*Plus Jakarta Sans*, *DM Sans*) y tema Material 3 con modo oscuro reactivo.

---

## 👥 Integrantes - Grupo 9
* *Guillermo Manríquez*
* *Apolo Vallejos*
* *Vicente Guzmán*
