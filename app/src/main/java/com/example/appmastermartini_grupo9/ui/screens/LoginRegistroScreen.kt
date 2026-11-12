package com.example.appmastermartini_grupo9.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
import com.example.appmastermartini_grupo9.viewmodel.EstadoViewModel
import com.example.appmastermartini_grupo9.viewmodel.MainViewModel
import com.example.appmastermartini_grupo9.viewmodel.PerfilViewModel
import com.example.appmastermartini_grupo9.viewmodel.UsuarioViewModel

/**
 * Modo de visualización de la pantalla de autenticación
 */
enum class ModoAuth {
    LOGIN,
    REGISTRO
}

/**
 * =========================================================================
 * PANTALLA DE INICIO DE SESIÓN Y REGISTRO — MASTER MARTINI CHILE
 * Diseño estándar y profesional de aplicación móvil:
 * - Logo oficial Master Martini superior centrado.
 * - Selector moderno de pestañas [Iniciar Sesión] | [Registrarse].
 * - Formularios completamente en español con etiquetas claras y placeholders amigables.
 * - Campos con diseño Material3 accesible (borde visible, altura adaptable para adultos mayores).
 * - Indicadores accesibles de carga en acciones largas.
 * =========================================================================
 */
@Composable
fun LoginRegistroScreen(
    navController: NavController = rememberNavController(),
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    perfilViewModel: PerfilViewModel? = null,
    estadoViewModel: EstadoViewModel? = null,
    modoInicial: ModoAuth = ModoAuth.LOGIN
) {
    val context = LocalContext.current
    var modoActual by rememberSaveable { mutableStateOf(modoInicial) }

    // Si el usuario está en Registro y pulsa volver atrás, retorna suavemente a Login
    BackHandler(enabled = modoActual == ModoAuth.REGISTRO) {
        modoActual = ModoAuth.LOGIN
    }

    // Diálogo de recuperación de contraseña
    var mostrarDialogoRecuperar by remember { mutableStateOf(false) }
    var emailRecuperarDialogo by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (modoActual == ModoAuth.REGISTRO || navController.previousBackStackEntry != null) {
                IconButton(
                    onClick = {
                        if (modoActual == ModoAuth.REGISTRO) modoActual = ModoAuth.LOGIN
                        else navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Logo oficial Master Martini
            Image(
                painter = painterResource(id = R.drawable.logo_nb),
                contentDescription = "Logo Master Martini",
                modifier = Modifier
                    .width(190.dp)
                    .height(60.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector moderno de pestañas: Iniciar Sesión / Registrarse
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (modoActual == ModoAuth.LOGIN) MaterialTheme.colorScheme.surface else Color.Transparent)
                        .clickable { modoActual = ModoAuth.LOGIN }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        fontWeight = if (modoActual == ModoAuth.LOGIN) FontWeight.Bold else FontWeight.Medium,
                        color = if (modoActual == ModoAuth.LOGIN) MartiniGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (modoActual == ModoAuth.REGISTRO) MaterialTheme.colorScheme.surface else Color.Transparent)
                        .clickable { modoActual = ModoAuth.REGISTRO }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Crear Cuenta",
                        fontWeight = if (modoActual == ModoAuth.REGISTRO) FontWeight.Bold else FontWeight.Medium,
                        color = if (modoActual == ModoAuth.REGISTRO) MartiniGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedContent(
                targetState = modoActual,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "AnimacionAuth"
            ) { modo ->
                if (modo == ModoAuth.LOGIN) {
                    FormularioLogin(
                        onForgotPasswordClick = { emailIngresado ->
                            emailRecuperarDialogo = emailIngresado
                            mostrarDialogoRecuperar = true
                        },
                        onLoginSubmit = { email, password ->
                            val nombreInferido = email.substringBefore("@")
                                .replace(".", " ")
                                .split(" ")
                                .filter { it.isNotBlank() }
                                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                                .ifBlank { "Pastelero" }
                            val handleInferido = email.substringBefore("@")
                            mainViewModel.iniciarSesionConDatos(
                                nombre = nombreInferido,
                                correo = email,
                                handle = handleInferido
                            )
                            estadoViewModel?.guardarSesion(
                                iniciada = true,
                                nombre = nombreInferido,
                                correo = email,
                                handle = handleInferido
                            )
                            Toast.makeText(context, "¡Bienvenido, $nombreInferido!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onIrARegistro = { modoActual = ModoAuth.REGISTRO },
                        onExplorarComoInvitado = {
                            mainViewModel.entrarComoInvitado()
                            Toast.makeText(context, "Modo invitado: Explorando Descubre", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                } else {
                    FormularioRegistro(
                        onSignUpSubmit = { nombre, email, password, phone, direccion, rut ->
                            val handleLimpio = email.substringBefore("@")
                            mainViewModel.registrarNuevoUsuario(
                                nombre = nombre,
                                handle = handleLimpio,
                                correo = email,
                                telefono = phone,
                                especialidad = "Pastelería Artesanal"
                            )
                            estadoViewModel?.guardarSesion(
                                iniciada = true,
                                nombre = nombre,
                                correo = email,
                                handle = handleLimpio
                            )
                            usuarioViewModel.onNombreChange(nombre)
                            usuarioViewModel.onHandleChange(handleLimpio)
                            usuarioViewModel.onCorreoChange(email)
                            usuarioViewModel.onClaveChange(password)
                            usuarioViewModel.onTelefonoChange(phone)
                            usuarioViewModel.onDireccionChange(direccion)
                            usuarioViewModel.onRutChange(rut)
                            perfilViewModel?.limpiarImagen()
                            Toast.makeText(
                                context,
                                "¡Bienvenido a Master Martini, $nombre!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onBackToLogin = { modoActual = ModoAuth.LOGIN }
                    )
                }
            }
        }
    }

    // Diálogo informativo para recuperar contraseña
    if (mostrarDialogoRecuperar) {
        val emailDestino = emailRecuperarDialogo.ifBlank { "tu correo registrado" }
        AlertDialog(
            onDismissRequest = { mostrarDialogoRecuperar = false },
            title = {
                Text(
                    text = "Recuperar Contraseña",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Enviaremos un enlace para restablecer tu contraseña al correo electrónico:\n\n$emailDestino",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoRecuperar = false
                        Toast.makeText(
                            context,
                            "Enlace enviado a $emailDestino",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen)
                ) {
                    Text("Enviar correo", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoRecuperar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * =========================================================================
 * FORMULARIO DE LOGIN CON FORMATO MÓVIL ESTÁNDAR
 * =========================================================================
 */
@Composable
private fun FormularioLogin(
    onForgotPasswordClick: (String) -> Unit,
    onLoginSubmit: (email: String, pass: String) -> Unit,
    onIrARegistro: () -> Unit,
    onExplorarComoInvitado: () -> Unit = {}
) {
    val colorIcono = MartiniGreen.copy(alpha = 0.7f)

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var procesandoLogin by remember { mutableStateOf(false) }

    fun validar(): Boolean {
        var valido = true
        val emailLimpio = email.trim()
        if (emailLimpio.isBlank()) {
            emailError = "Ingresa tu correo electrónico"
            valido = false
        } else if (!emailLimpio.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            emailError = "Ingresa un correo válido (ej. usuario@dominio.cl)"
            valido = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = "Ingresa tu contraseña"
            valido = false
        } else if (password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            valido = false
        } else {
            passwordError = null
        }

        return valido
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Campo Correo electrónico
        AuthTextField(
            label = "Correo electrónico",
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) emailError = null
            },
            placeholder = "ejemplo@mastermartini.cl",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Correo",
                    tint = if (emailError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            isError = emailError != null,
            errorMessage = emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Campo Contraseña
        AuthTextField(
            label = "Contraseña",
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) passwordError = null
            },
            placeholder = "Ingresa tu contraseña",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Contraseña",
                    tint = if (passwordError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = colorIcono,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            errorMessage = passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Enlace "¿Olvidaste tu contraseña?"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 20.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = MartiniGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onForgotPasswordClick(email) }
                    .padding(4.dp)
            )
        }

        // Botón principal "Iniciar Sesión"
        Button(
            onClick = {
                if (validar() && !procesandoLogin) {
                    procesandoLogin = true
                    onLoginSubmit(email.trim(), password)
                }
            },
            enabled = !procesandoLogin,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MartiniGreen,
                disabledContainerColor = MartiniGreen.copy(alpha = 0.65f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (procesandoLogin) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Iniciando sesión...",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (procesandoLogin) {
            Text(
                text = "Abriendo la aplicación...",
                style = MaterialTheme.typography.bodySmall,
                color = MartiniGreenDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Enlace inferior: "¿No tienes una cuenta? Regístrate"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Text(
                text = "Regístrate",
                color = MartiniGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onIrARegistro)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        // Acceso directo para explorar como invitado
        TextButton(
            onClick = onExplorarComoInvitado,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "Explorar como invitado",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * =========================================================================
 * FORMULARIO DE REGISTRO CON FORMATO MÓVIL ESTÁNDAR
 * =========================================================================
 */
@Composable
private fun FormularioRegistro(
    onSignUpSubmit: (nombre: String, email: String, pass: String, phone: String, direccion: String, rut: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    val colorIcono = MartiniGreen.copy(alpha = 0.7f)

    var nombre by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var phone by rememberSaveable { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }

    var nombreError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var procesandoRegistro by remember { mutableStateOf(false) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var phoneError by rememberSaveable { mutableStateOf<String?>(null) }
    var direccionError by remember { mutableStateOf<String?>(null) }
    var rutError by remember { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        var valido = true

        val nomLimpio = nombre.trim()
        if (nomLimpio.isBlank()) {
            nombreError = "Ingresa tu nombre completo"
            valido = false
        } else if (nomLimpio.count { it.isLetter() } < 2) {
            nombreError = "Ingresa un nombre válido"
            valido = false
        } else {
            nombreError = null
        }

        val emailLimpio = email.trim()
        if (emailLimpio.isBlank()) {
            emailError = "Ingresa tu correo electrónico"
            valido = false
        } else if (!emailLimpio.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            emailError = "Ingresa un correo válido (ej. usuario@dominio.cl)"
            valido = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = "Crea una contraseña"
            valido = false
        } else if (password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            valido = false
        } else {
            passwordError = null
        }

        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Confirma tu contraseña"
            valido = false
        } else if (confirmPassword != password) {
            confirmPasswordError = "Las contraseñas no coinciden"
            valido = false
        } else {
            confirmPasswordError = null
        }

        phoneError = when {
            phone.isBlank() -> "Ingresa tu teléfono de contacto"
            normalizarTelefonoChileno(phone) == null -> "Ingresa un teléfono chileno de 9 dígitos"
            else -> null
        }
        direccionError = when {
            direccion.isBlank() -> "Ingresa tu dirección"
            direccion.none { it.isLetter() } -> "Escribe el nombre de la calle o localidad"
            else -> null
        }
        rutError = when {
            rut.isBlank() -> "Ingresa tu RUT"
            normalizarRut(rut) == null -> "Revisa el RUT y su dígito verificador"
            else -> null
        }

        return valido && phoneError == null && direccionError == null && rutError == null
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Nombre Completo
        AuthTextField(
            label = "Nombre completo",
            value = nombre,
            onValueChange = {
                nombre = it
                if (nombreError != null) nombreError = null
            },
            placeholder = "ej. Carlos Morales",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Nombre",
                    tint = if (nombreError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            isError = nombreError != null,
            errorMessage = nombreError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Correo electrónico
        AuthTextField(
            label = "Correo electrónico",
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) emailError = null
            },
            placeholder = "carlos@pasteleria.cl",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email",
                    tint = if (emailError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            isError = emailError != null,
            errorMessage = emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Teléfono
        AuthTextField(
            label = "Teléfono de contacto",
            value = phone,
            onValueChange = {
                phone = it
                if (phoneError != null) phoneError = null
            },
            placeholder = "+56 9 1234 5678",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Teléfono",
                    tint = if (phoneError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            isError = phoneError != null,
            errorMessage = phoneError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            label = "Dirección",
            value = direccion,
            onValueChange = {
                direccion = it
                direccionError = null
            },
            placeholder = "Calle, número y comuna",
            isError = direccionError != null,
            errorMessage = direccionError
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            label = "RUT",
            value = rut,
            onValueChange = {
                rut = it
                rutError = null
            },
            placeholder = "12.345.678-5",
            isError = rutError != null,
            errorMessage = rutError
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 5. Contraseña
        AuthTextField(
            label = "Contraseña",
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) passwordError = null
            },
            placeholder = "Mínimo 6 caracteres",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Contraseña",
                    tint = if (passwordError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar contraseña",
                        tint = colorIcono,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            errorMessage = passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 6. Confirmar Contraseña
        AuthTextField(
            label = "Confirmar contraseña",
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                if (confirmPasswordError != null) confirmPasswordError = null
            },
            placeholder = "Repite tu contraseña",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Confirmar contraseña",
                    tint = if (confirmPasswordError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar confirmación",
                        tint = colorIcono,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón principal "Crear Cuenta"
        Button(
            onClick = {
                if (validar() && !procesandoRegistro) {
                    procesandoRegistro = true
                    onSignUpSubmit(
                        nombre.trim(), email.trim(), password, normalizarTelefonoChileno(phone).orEmpty(),
                        direccion.trim(), normalizarRut(rut).orEmpty()
                    )
                }
            },
            enabled = !procesandoRegistro,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MartiniGreen,
                disabledContainerColor = MartiniGreen.copy(alpha = 0.65f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (procesandoRegistro) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Creando cuenta...",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Crear Cuenta",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (procesandoRegistro) {
            Text(
                text = "Registrando tu perfil pastelero en Master Martini...",
                style = MaterialTheme.typography.bodySmall,
                color = MartiniGreenDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Enlace para volver a Iniciar Sesión
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "¿Ya tienes una cuenta? ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Text(
                text = "Inicia sesión",
                color = MartiniGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onBackToLogin)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

/**
 * =========================================================================
 * CAMPO DE TEXTO ESTÁNDAR PARA AUTENTICACIÓN MÓVIL
 * =========================================================================
 */
private val formatoTelefono = Regex("[+]?[0-9 ()-]+")
private val numeroChileno = Regex("[2-9][0-9]{8}")
private val formatoRut = Regex("(?:[0-9]{1,8}|[0-9]{1,3}(?:\\.[0-9]{3}){1,2})-?[0-9K]")

private fun normalizarTelefonoChileno(telefono: String): String? {
    val entrada = telefono.trim()
    if (!formatoTelefono.matches(entrada)) return null
    val digitos = entrada.filter { it in '0'..'9' }
    val nacional = when {
        digitos.length == 11 && digitos.startsWith("56") -> digitos.drop(2)
        !entrada.startsWith("+") -> digitos
        else -> return null
    }
    return if (numeroChileno.matches(nacional)) "+56$nacional" else null
}

private fun normalizarRut(rut: String): String? {
    val entrada = rut.trim().uppercase()
    if (!formatoRut.matches(entrada)) return null
    val compacto = entrada.filterNot { it == '.' || it == '-' }
    val cuerpo = compacto.dropLast(1)
    if (cuerpo.length > 8 || cuerpo.all { it == '0' }) return null
    val suma = cuerpo.reversed().mapIndexed { indice, digito ->
        digito.digitToInt() * (2 + indice % 6)
    }.sum()
    val verificador = when (val resto = 11 - suma % 11) {
        11 -> '0'
        10 -> 'K'
        else -> resto.digitToChar()
    }
    return if (compacto.last() == verificador) "$cuerpo-$verificador" else null
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                modifier = Modifier.padding(bottom = 5.dp, start = 2.dp)
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    fontSize = 14.sp
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true,
            isError = isError,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                focusedBorderColor = MartiniGreen,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = MartiniGreen
            ),
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 6.dp, top = 4.dp)
            )
        }
    }
}
