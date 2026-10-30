# Master Martini Chile — Aplicación Móvil Profesional
> **Proyecto Semestral · Desarrollo de Aplicaciones Móviles (DSY1105)**  
> **Duoc UC — Grupo 9**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20UDF-brightgreen.svg)]()
[![Platform](https://img.shields.io/badge/Platform-Android%20(API%2024%2B)-success.svg?logo=android)](https://www.android.com)

Aplicación móvil nativa para Android desarrollada con **Jetpack Compose** y arquitectura **MVVM (Model-View-ViewModel)** para **Master Martini Chile** ([mastermartini.cl](https://mastermartini.cl/)), filial oficial del Grupo Unigrà líder en soluciones e insumos vegetales para chocolatería, pastelería, panadería y heladería profesional.

El proyecto fusiona la experiencia de un catálogo técnico B2B con un ecosistema comunitario y formativo inspirado en el dinamismo visual de **Pinterest** y la interacción técnica de **Threads**.

---

## 👥 Integrantes — Grupo 9
* **Guillermo Manríquez**
* **Apolo Vallejos**
* **Vicente Guzmán**

---

## 📌 Planificación y Gestión del Proyecto
El flujo de trabajo, las historias de usuario y la distribución de tareas se gestionaron mediante metodología ágil en **Trello**:

🔗 **[Tablero de Planificación en Trello — Master Martini Grupo 9](https://trello.com/b/LjtchmiV/master-martini)**

* 📋 **Por hacer (To Do):** Requisitos técnicos, arquitectura base y diseño de pantallas.
* ⏳ **En curso (Doing):** Implementación de módulos, validaciones y conexión de recursos nativos.
* ✅ **Finalizado (Done):** Pruebas unitarias, optimización de ciclo de vida, persistencia y documentación.

---

## 📱 Módulos y Funcionalidades Principales

### 1. 🌟 Descubre (Mosaico de Recetas & Pins Multimedia)
* **Mosaico escalonado dinámico (2 columnas):** Convivencia armónica de recetas oficiales verificadas (*Sello ✓ Martini*) y creaciones compartidas por la comunidad.
* **Reproductor de Video Integrado:** Integración con **AndroidX Media3 (ExoPlayer)** para recetas con formato audiovisual HD (como las recetas del creador invitado *Don Saco Roto*), con soporte táctil de play/pausa, silenciador flotante y pausa automática vinculada al ciclo de vida del dispositivo (`ON_PAUSE`).
* **Filtros por categoría técnica:** Chocolatería, Pastelería, Bollería, Gelato y Salados, junto con búsqueda reactiva en tiempo real por título, autor o ingredientes.

### 2. 📦 Catálogo Profesional de Productos
* Fichas interactivas expandibles de las principales líneas de insumos de Grupo Unigrà:
  * **Ariba:** Coberturas y chocolates puros de manteca de cacao seleccionada.
  * **Caravella:** Cremas anhidras y sucedáneas untables para relleno y horneo.
  * **Decor Up:** Crema vegetal UHT de alto rendimiento para montado y vitrina.
  * **OraSì:** Bebidas vegetales barista para latte art y heladería 100% plant-based.
  * **Maxime:** Margarinas técnicas en placa para hojaldres y masas laminadas.
* Vinculación bidireccional entre cada producto y las recetas oficiales donde se utiliza.

### 3. ➕ Creador de Contenido (Receta o Consulta de Taller)
* Selector intuitivo de doble propósito:
  * **Subir Receta:** Formulario completo para registrar nombre, descripción, porciones, insumos utilizados, tiempo y pasos de elaboración.
  * **Consulta Técnica / Foro:** Publicación ágil para resolver dudas sobre puntos de batido, temperaturas de cristalización o rendimiento en obrador.
* Carga de portada y video mediante cámara fotográfica nativa o selector de galería local.

### 4. 🎓 Master Martini Academy (Cursos y Capacitación)
* Catálogo de cursos gastronómicos clasificados en módulos gratuitos y de especialización (*Academy Pro*).
* Indicador visual de progreso porcentual por curso (`LinearProgressIndicator`).
* Detalle de lecciones interactivas con marcado dinámico de clases completadas y persistencia en memoria.

### 5. 💬 Comunidad & Foro Técnico de Pasteleros
* Muro de conversación con hilos de respuestas continuas conectadas por una guía visual vertical (estilo Threads).
* Acciones sociales reactivas: Me gusta (`Like`), republicar (`Repost`), guardar en colecciones y compartir mediante el menú nativo del sistema operativo (`Intent.ACTION_SEND`).
* Filtros rápidos entre *"Para ti"* y *"Siguiendo"* (donde destacan maestros pasteleros y chefs técnicos oficiales).

### 6. 👤 Mi Espacio (Perfil de Usuario y Colecciones)
* Visualización de recetas propias subidas y recetas guardadas.
* **Colecciones con vista previa deslizable (*Swipeable Nested Carousel*):** Carrusel horizontal interno dentro de cada tarjeta de carpeta para previsualizar recetas guardadas sin necesidad de ingresar.
* **Integración nativa con Cámara:** Captura fotográfica segura con `FileProvider` (`ActivityResultContracts.TakePicture`) y selección desde almacenamiento local (`ActivityResultContracts.GetContent`).
* **Panel de Configuración:** Ajustes de cuenta, preferencias de unidades métricas, permisos de la app en Android y alternancia de Modo Oscuro.

### 7. 🔐 Autenticación y Registro con Validación Robusta
* **Modo Login y Modo Registro** con transición animada fluida (`AnimatedContent`).
* **Validaciones desacopladas por campo:**
  * Nombre y `@handle` con verificación de caracteres y formato.
  * Correo electrónico con validación de sintaxis estándar (`Patterns.EMAIL_ADDRESS`).
  * Contraseñas con longitud mínima de seguridad y comprobación estricta de coincidencia.
  * Teléfono con filtro numérico.
* **Retroalimentación visual accesible:** Campos con iluminación en tono de alerta suave (`#FFF6F6`), bordes en color de error del sistema y mensajes descriptivos bajo el campo afectado.
* Opciones de recuperación de contraseña simulada y acceso rápido con perfiles sociales.

---

## 🏛️ Arquitectura del Software (MVVM + UDF)

El proyecto está estructurado siguiendo las recomendaciones oficiales de arquitectura de Google para aplicaciones modernas en Android:

```
app/src/main/java/com/example/appmastermartini_grupo9/
├── data/               # Persistencia local con DataStore Preferences
│   └── EstadoDataStore.kt
├── model/              # Modelos de datos inmutables y estados de UI
│   ├── GastronomiaModels.kt
│   ├── ProductItem.kt
│   └── UsuarioUiState.kt
├── navigation/         # Navegación tipada y eventos desacoplados
│   ├── AppNavigation.kt
│   ├── NavigationEvent.kt
│   └── Screen.kt
├── repository/         # Fuente de datos de catálogo e insumos
│   └── ProductRepository.kt
├── ui/
│   ├── components/     # Componentes modulares reutilizables
│   │   ├── FloatingBottomBar.kt
│   │   ├── ImagenInteligente.kt
│   │   ├── MartiniTopHeader.kt
│   │   └── MartiniVideoPlayer.kt
│   ├── screens/        # Vistas de la aplicación (Composables)
│   │   ├── ComunidadScreen.kt
│   │   ├── CrearRecetaScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── HomeScreenAdaptive.kt
│   │   ├── LoginRegistroScreen.kt
│   │   ├── PantallaAnimaciones.kt
│   │   ├── PerfilCreadorScreen.kt
│   │   ├── PerfilFotoScreen.kt
│   │   ├── ProfileScreen.kt
│   │   ├── RecetaDetalleScreen.kt
│   │   ├── RegistroScreen.kt
│   │   ├── ResumenScreen.kt
│   │   ├── SettingsScreen.kt
│   │   └── SplashScreen.kt
│   ├── theme/          # Sistema de diseño, colores institucionales y tipografía
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── utils/          # Utilidades de adaptabilidad (WindowSizeClass)
│       └── WindowSizeUtils.kt
└── viewmodel/          # ViewModels y gestión de estado reactivo
    ├── EstadoViewModel.kt
    ├── MainViewModel.kt
    ├── PerfilViewModel.kt
    └── UsuarioViewModel.kt
```

### Principios y patrones aplicados:
* **Separación de Responsabilidades:** La interfaz de usuario (`UI`) es completamente declarativa y no contiene lógica de negocio ni persistencia directa.
* **Unidirectional Data Flow (UDF):** El estado se expone hacia la vista mediante `StateFlow` inmutable y los eventos del usuario se canalizan a través de llamadas a métodos del `ViewModel`.
* **Persistencia Reactiva:** Uso de **Jetpack DataStore Preferences** para guardar de manera asíncrona y reactiva las preferencias del usuario (modo oscuro y personalización) a través de corrutinas de Kotlin.
* **Navegación desacoplada:** El `MainViewModel` emite eventos de navegación mediante `SharedFlow<NavigationEvent>`, desacoplando las acciones de negocio del `NavController`.
* **Acceso seguro a hardware nativo:**
  * `FileProvider` con archivo seguro `file_paths.xml` para compartir URIs de cámara temporales sin exponer rutas directas del almacenamiento.
  * Contratos modernos de Activity Result (`ActivityResultContracts.TakePicture` y `GetContent`) evitando la sobreescritura de métodos obsoletos como `onActivityResult`.

---

## 🎨 Sistema de Diseño Visual e Identidad

* **Paleta de Colores Corporativa:**
  * `MartiniGreen` (`#004D42`): Verde institucional principal.
  * `MartiniDarkKhaki` (`#B69F58`) / `MartiniKhaki` (`#FDF69E`): Dorados y tonos cálidos del emblema oficial.
  * `MartiniFloralWhite` (`#F8F8F2`): Fondo cálido limpio para interfaces gastronómicas.
  * Soporte nativo para **Tema Claro y Modo Oscuro**.
* **Tipografías:**
  * **Plus Jakarta Sans:** Títulos, encabezados y elementos de marca con curvas geométricas modernas.
  * **DM Sans:** Lectura descansada para descripciones, ingredientes y buscadores.
* **Animaciones Funcionales:**
  * `Animatable` para la secuencia de inicio en `SplashScreen` (zoom del emblema, fade in y despliegue de línea dorada).
  * `AnimatedContent` para transiciones suaves entre login y registro.
  * `animateColorAsState` para transiciones de estado en la barra de navegación flotante.
  * `AnimatedVisibility` para mensajes de confirmación y visibilidad de controles.

---

## 🛠️ Requisitos del Entorno y Pasos para Ejecutar

### Requisitos mínimos:
* **Android Studio:** Ladybug (2024.2.1) o superior.
* **Java Development Kit (JDK):** Versión 11 o superior.
* **Android SDK:** `minSdk = 24` (Android 7.0 Nougat) hasta `targetSdk = 37`.
* Dispositivo físico Android con depuración USB activada o Emulador oficial (ej. Pixel 7 / Pixel 8 con API 34+).

### Pasos de instalación y ejecución:

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/vguzc/AppMasterMartini_Grupo9.git
   ```
2. **Abrir el proyecto:**
   * Abrir Android Studio.
   * Seleccionar **File > Open...** y elegir la carpeta clonada `AppMasterMartini_Grupo9`.
3. **Sincronizar dependencias Gradle:**
   * Permitir que Android Studio sincronice las dependencias del archivo `build.gradle.kts`.
4. **Ejecutar la aplicación:**
   * Seleccionar el dispositivo de destino (emulador o físico).
   * Presionar el botón **Run 'app'** (`Shift + F10`) o ejecutar desde la terminal:
     ```bash
     ./gradlew assembleDebug
     ```
5. **Ejecutar las pruebas unitarias:**
   ```bash
   ./gradlew testDebugUnitTest
   ```

---

## 📄 Licencia y Contexto
Proyecto desarrollado exclusivamente con fines formativos y académicos para la asignatura de **Desarrollo de Aplicaciones Móviles (DSY1105)** en **Duoc UC**. Todos los derechos de marca, logotipos y nombres de productos pertenecen a **Master Martini Chile / Grupo Unigrà**.
