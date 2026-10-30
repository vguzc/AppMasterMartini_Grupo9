package com.example.appmastermartini_grupo9.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
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
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark
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
 * PANTALLA DE LOGIN Y REGISTRO (INSPIRADA EN PLANTLAND / ESTILO MASTER MARTINI)
 * - Cabecera curva orgánica en verde Martini (#1B4D3E) con formas fluidas vectoriales.
 * - Sin fotos preexistentes de perfil o pastelería en cuentas no registradas.
 * - Tarjeta blanca inferior con bordes redondeados (34.dp).
 * - Campos con diseño redondeado de cápsula (#F4F6F5) e iconos lineales.
 * - Selección dedicada de Nombre de Usuario (@handle).
 * - Validaciones completas con mensajes y bordes en color de error (#BA1A1A).
 * - Sincronización limpia con el usuario activo en Comunidad y Perfil.
 * =========================================================================
 */
@Composable
fun LoginRegistroScreen(
    navController: NavController = rememberNavController(),
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    perfilViewModel: PerfilViewModel? = null,
    modoInicial: ModoAuth = ModoAuth.LOGIN
) {
    val context = LocalContext.current
    var modoActual by rememberSaveable { mutableStateOf(modoInicial) }

    // Diálogo de recuperación de contraseña
    var mostrarDialogoRecuperar by remember { mutableStateOf(false) }
    var emailRecuperarDialogo by remember { mutableStateOf("") }

    val verdeFondoOscuro = Color(0xFF0F3529)
    val verdeFondoClaro = Color(0xFF1B4D3E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(verdeFondoOscuro, verdeFondoClaro)
                )
            )
    ) {
        // Formas orgánicas abstractas en la esquina superior izquierda
        Canvas(
            modifier = Modifier
                .size(190.dp)
                .offset(x = (-30).dp, y = (-20).dp)
        ) {
            val w = size.width
            val h = size.height

            // Capa exterior suave (verde menta translúcido)
            val pathExterior = Path().apply {
                moveTo(0f, 0f)
                lineTo(w * 0.9f, 0f)
                cubicTo(
                    w * 1.05f, h * 0.40f,
                    w * 0.70f, h * 0.85f,
                    w * 0.35f, h * 0.98f
                )
                cubicTo(
                    w * 0.15f, h * 1.02f,
                    0f, h * 0.80f,
                    0f, h * 0.60f
                )
                close()
            }
            drawPath(pathExterior, color = Color(0xFF6FAF98).copy(alpha = 0.55f))

            // Capa interior con curva tipo hoja / frijol
            val pathInterior = Path().apply {
                moveTo(w * 0.18f, 0f)
                cubicTo(
                    w * 0.65f, h * 0.22f,
                    w * 0.60f, h * 0.68f,
                    0f, h * 0.65f
                )
                lineTo(0f, 0f)
                close()
            }
            drawPath(pathInterior, color = Color(0xFF8DC6B2).copy(alpha = 0.42f))
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // =========================================================================
            // CABECERA SUPERIOR (LIMPIA, SIN FOTO DE PERFIL / FORO PREEXISTENTE)
            // =========================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(if (modoActual == ModoAuth.LOGIN) 165.dp else 145.dp)
            ) {
                if (modoActual == ModoAuth.LOGIN) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 28.dp, end = 28.dp, bottom = 10.dp)
                    ) {
                        Text(
                            text = "¡Hola!",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bienvenido a Master Martini",
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                } else {
                    // Cabecera en Registro: botón "← Volver al login" y título de bienvenida
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 22.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { modoActual = ModoAuth.LOGIN }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver a Iniciar Sesión",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Volver al inicio de sesión",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Crea tu cuenta pastelera",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Text(
                            text = "Únete a la comunidad de maestros pasteleros",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }
                }
            }

            // =========================================================================
            // TARJETA BLANCA PRINCIPAL (FORMULARIO CON TRANSICIÓN ANIMADA)
            // =========================================================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                AnimatedContent(
                    targetState = modoActual,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "AnimacionAuth"
                ) { modo ->
                    if (modo == ModoAuth.LOGIN) {
                        // VISTA DE LOGIN
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
                                Toast.makeText(context, "¡Sesión iniciada como $nombreInferido!", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onSocialLoginClick = { red ->
                                mainViewModel.iniciarSesionConDatos(
                                    nombre = "Usuario $red",
                                    correo = "usuario@$red.com",
                                    handle = "usuario_$red"
                                )
                                Toast.makeText(context, "Sesión iniciada con $red", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onIrARegistro = { modoActual = ModoAuth.REGISTRO }
                        )
                    } else {
                        // VISTA DE REGISTRO CON ELECCIÓN DE USUARIO Y VALIDACIONES
                        FormularioRegistro(
                            onSignUpSubmit = { nombre, handle, email, password, phone ->
                                val handleLimpio = handle.trim().removePrefix("@")
                                mainViewModel.registrarNuevoUsuario(
                                    nombre = nombre,
                                    handle = handleLimpio,
                                    correo = email,
                                    telefono = phone,
                                    especialidad = "Pastelería Artesanal"
                                )
                                usuarioViewModel.onNombreChange(nombre)
                                usuarioViewModel.onHandleChange(handleLimpio)
                                usuarioViewModel.onCorreoChange(email)
                                usuarioViewModel.onClaveChange(password)
                                usuarioViewModel.onTelefonoChange(phone)
                                perfilViewModel?.limpiarImagen()
                                Toast.makeText(
                                    context,
                                    "¡Bienvenido a Master Martini, $nombre (@$handleLimpio)!",
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
                    text = "Enviaremos un enlace de restablecimiento seguro al correo electrónico asociado:\n\n$emailDestino",
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
                    shape = RoundedCornerShape(50),
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
 * FORMULARIO DE LOGIN CON VALIDACIONES COMPLETAS
 * =========================================================================
 */
@Composable
private fun FormularioLogin(
    onForgotPasswordClick: (String) -> Unit,
    onLoginSubmit: (email: String, pass: String) -> Unit,
    onSocialLoginClick: (String) -> Unit,
    onIrARegistro: () -> Unit
) {
    val scrollState = rememberScrollState()
    val colorIcono = Color(0xFF8AA69C)

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        var valido = true
        val emailLimpio = email.trim()
        if (emailLimpio.isBlank()) {
            emailError = "Ingresa tu correo electrónico"
            valido = false
        } else if (!emailLimpio.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            emailError = "El correo debe incluir '@' y un dominio válido (ej. usuario@dominio.cl)"
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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 26.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título "Login"
        Text(
            text = "Login",
            color = MartiniGreenDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        // 1. Campo Email
        AuthTextField(
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) emailError = null
            },
            placeholder = "Email (ej. pastelero@mastermartini.cl)",
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

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Campo Password
        AuthTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) passwordError = null
            },
            placeholder = "Password (mín. 6 caracteres)",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Password",
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
                        modifier = Modifier.size(19.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            errorMessage = passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Enlace "Forgot Password" a la derecha
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 22.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Forgot Password",
                color = MartiniGreenDark,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onForgotPasswordClick(email) }
                    .padding(4.dp)
            )
        }

        // Botón principal "Login"
        Button(
            onClick = {
                if (validar()) {
                    onLoginSubmit(email.trim(), password)
                }
            },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divisor "Or login with"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE2E7E5),
                thickness = 1.dp
            )
            Text(
                text = "  Or login with  ",
                color = Color(0xFF9EAAA4),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE2E7E5),
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Chips de autenticación social
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialLoginChip(
                nombre = "Facebook",
                onClick = { onSocialLoginClick("Facebook") }
            ) {
                FacebookIcon(modifier = Modifier.size(24.dp))
            }

            SocialLoginChip(
                nombre = "Google",
                onClick = { onSocialLoginClick("Google") }
            ) {
                GoogleIcon(modifier = Modifier.size(22.dp))
            }

            SocialLoginChip(
                nombre = "Apple",
                onClick = { onSocialLoginClick("Apple") }
            ) {
                AppleIcon(modifier = Modifier.size(22.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Enlace inferior: "Don't have account? Sign Up"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Don't have account? ",
                color = Color(0xFF7A8B85),
                fontSize = 13.5.sp
            )
            Text(
                text = "Sign Up",
                color = MartiniGreenDark,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onIrARegistro)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

/**
 * =========================================================================
 * FORMULARIO DE REGISTRO CON ELECCIÓN DE USUARIO Y VALIDACIONES COMPLETAS
 * =========================================================================
 */
@Composable
private fun FormularioRegistro(
    onSignUpSubmit: (nombre: String, handle: String, email: String, pass: String, phone: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    val scrollState = rememberScrollState()
    val colorIcono = Color(0xFF8AA69C)

    var nombre by rememberSaveable { mutableStateOf("") }
    var handle by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var phone by rememberSaveable { mutableStateOf("") }

    // Errores individuales
    var nombreError by rememberSaveable { mutableStateOf<String?>(null) }
    var handleError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var phoneError by rememberSaveable { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        var valido = true

        // 1. Nombre
        val nomLimpio = nombre.trim()
        if (nomLimpio.isBlank()) {
            nombreError = "Ingresa tu nombre completo"
            valido = false
        } else if (nomLimpio.length < 3) {
            nombreError = "El nombre debe tener al menos 3 caracteres"
            valido = false
        } else {
            nombreError = null
        }

        // 2. Nombre de usuario / handle
        val hLimpio = handle.trim().removePrefix("@")
        if (hLimpio.isBlank()) {
            handleError = "Escoge tu nombre de usuario para la comunidad"
            valido = false
        } else if (hLimpio.length < 3) {
            handleError = "El usuario debe tener al menos 3 caracteres"
            valido = false
        } else if (hLimpio.contains(" ")) {
            handleError = "El nombre de usuario no puede contener espacios"
            valido = false
        } else if (!hLimpio.matches(Regex("^[a-zA-Z0-9_.]+$"))) {
            handleError = "Usa solo letras, números, puntos o guiones bajos"
            valido = false
        } else {
            handleError = null
        }

        // 3. Email
        val emailLimpio = email.trim()
        if (emailLimpio.isBlank()) {
            emailError = "Ingresa tu correo electrónico"
            valido = false
        } else if (!emailLimpio.contains("@")) {
            emailError = "El correo debe contener el símbolo '@'"
            valido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            emailError = "Formato de correo no válido (ej. pastelero@dominio.cl)"
            valido = false
        } else {
            emailError = null
        }

        // 4. Password
        if (password.isBlank()) {
            passwordError = "Crea una contraseña"
            valido = false
        } else if (password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            valido = false
        } else {
            passwordError = null
        }

        // 5. Confirm Password
        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Confirma tu contraseña"
            valido = false
        } else if (confirmPassword != password) {
            confirmPasswordError = "Las contraseñas no coinciden"
            valido = false
        } else {
            confirmPasswordError = null
        }

        // 6. Phone
        val phoneLimpio = phone.trim()
        val digitos = phoneLimpio.filter { it.isDigit() }
        if (phoneLimpio.isBlank()) {
            phoneError = "Ingresa tu número telefónico"
            valido = false
        } else if (digitos.length < 8) {
            phoneError = "Ingresa un teléfono válido (al menos 8 dígitos)"
            valido = false
        } else {
            phoneError = null
        }

        return valido
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 26.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Enlace "← Back to login"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .clickable(onClick = onBackToLogin)
                .padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MartiniGreenDark,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Back to login",
                color = MartiniGreenDark,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Título "Sign Up"
        Text(
            text = "Sign Up",
            color = MartiniGreenDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // 1. Campo Nombre Completo
        AuthTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                if (nombreError != null) nombreError = null
            },
            placeholder = "Nombre completo (ej. Carlos Morales)",
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

        // 2. Campo Nombre de Usuario (@handle)
        AuthTextField(
            value = handle,
            onValueChange = {
                handle = it
                if (handleError != null) handleError = null
            },
            placeholder = "Nombre de usuario (ej. @carlos_pasteles)",
            leadingIcon = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp)) {
                    Text(
                        text = "@",
                        color = if (handleError != null) MaterialTheme.colorScheme.error else colorIcono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            },
            isError = handleError != null,
            errorMessage = handleError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Campo Email
        AuthTextField(
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) emailError = null
            },
            placeholder = "Email (ej. carlos@pasteleria.cl)",
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

        // 4. Campo Password
        AuthTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) passwordError = null
            },
            placeholder = "Password (mín. 6 caracteres)",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Password",
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
                        modifier = Modifier.size(19.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            errorMessage = passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 5. Campo Confirm Password
        AuthTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                if (confirmPasswordError != null) confirmPasswordError = null
            },
            placeholder = "Confirm Password",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Confirm Password",
                    tint = if (confirmPasswordError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar confirmación de contraseña",
                        tint = colorIcono,
                        modifier = Modifier.size(19.dp)
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 6. Campo Phone
        AuthTextField(
            value = phone,
            onValueChange = {
                phone = it
                if (phoneError != null) phoneError = null
            },
            placeholder = "Phone (ej. +56 9 1234 5678)",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Phone",
                    tint = if (phoneError != null) MaterialTheme.colorScheme.error else colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            },
            isError = phoneError != null,
            errorMessage = phoneError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Botón principal "Sign Up"
        Button(
            onClick = {
                if (validar()) {
                    onSignUpSubmit(nombre.trim(), handle.trim(), email.trim(), password, phone.trim())
                }
            },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MartiniGreen),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Sign Up",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Enlace inferior para volver a Login
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "Already have an account? ",
                color = Color(0xFF7A8B85),
                fontSize = 13.5.sp
            )
            Text(
                text = "Login",
                color = MartiniGreenDark,
                fontSize = 13.5.sp,
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
 * Campo de texto estilizado tipo píldora con validación reactiva y mensaje de error
 */
@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    val colorCampoBg = Color(0xFFF4F6F5)
    val colorErrorBg = Color(0xFFFFF6F6)

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF9EAAA4), fontSize = 13.5.sp) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true,
            isError = isError,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = if (isError) colorErrorBg else colorCampoBg,
                unfocusedContainerColor = if (isError) colorErrorBg else colorCampoBg,
                focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MartiniGreen,
                unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.7f) else Color.Transparent,
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = if (isError) MaterialTheme.colorScheme.error else MartiniGreen,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.5.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Chip contenedor para los botones sociales (Facebook, Google, Apple)
 */
@Composable
private fun SocialLoginChip(
    nombre: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE4E8E6)),
        shadowElevation = 1.dp,
        modifier = Modifier.size(54.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }
    }
}

/**
 * Ícono de Facebook (círculo azul con letra 'f' blanca)
 */
@Composable
private fun FacebookIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFF1877F2)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "f",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y = (-1).dp)
        )
    }
}

/**
 * Ícono vectorial oficial de Google con sus 4 colores
 */
@Composable
private fun GoogleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.minDimension
        val stroke = s * 0.18f
        val radius = (s - stroke) / 2f
        val center = Offset(s / 2f, s / 2f)

        // Rojo (superior)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = -45f,
            sweepAngle = -110f,
            useCenter = false,
            topLeft = Offset(stroke / 2, stroke / 2),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        // Amarillo (lateral izquierdo)
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = -155f,
            sweepAngle = -70f,
            useCenter = false,
            topLeft = Offset(stroke / 2, stroke / 2),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        // Verde (inferior)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = -225f,
            sweepAngle = -90f,
            useCenter = false,
            topLeft = Offset(stroke / 2, stroke / 2),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        // Azul (cuadrante inferior derecho)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = 45f,
            sweepAngle = -50f,
            useCenter = false,
            topLeft = Offset(stroke / 2, stroke / 2),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Butt)
        )
        // Barra horizontal azul central
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(center.x, center.y),
            end = Offset(center.x + radius + stroke / 2, center.y),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Ícono de Apple (silueta negra vectorizada con hoja)
 */
@Composable
private fun AppleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Cuerpo de la manzana
        val manzana = Path().apply {
            moveTo(w * 0.50f, h * 0.28f)
            cubicTo(w * 0.60f, h * 0.28f, w * 0.72f, h * 0.22f, w * 0.80f, h * 0.28f)
            cubicTo(w * 0.88f, h * 0.35f, w * 0.95f, h * 0.52f, w * 0.85f, h * 0.72f)
            cubicTo(w * 0.78f, h * 0.84f, w * 0.70f, h * 0.95f, w * 0.58f, h * 0.95f)
            cubicTo(w * 0.48f, h * 0.95f, w * 0.42f, h * 0.88f, w * 0.32f, h * 0.88f)
            cubicTo(w * 0.22f, h * 0.88f, w * 0.15f, h * 0.95f, w * 0.06f, h * 0.95f)
            cubicTo(-0.06f, h * 0.95f, -0.06f, h * 0.68f, 0.05f, h * 0.48f)
            cubicTo(0.14f, h * 0.32f, 0.28f, h * 0.24f, 0.42f, h * 0.24f)
            cubicTo(0.48f, h * 0.24f, w * 0.50f, h * 0.28f, w * 0.50f, h * 0.28f)
            close()
        }
        drawPath(manzana, color = Color(0xFF1E2421))

        // Hoja superior
        val hoja = Path().apply {
            moveTo(w * 0.52f, h * 0.22f)
            cubicTo(w * 0.58f, h * 0.12f, w * 0.68f, h * 0.05f, w * 0.68f, h * 0.05f)
            cubicTo(w * 0.68f, h * 0.05f, w * 0.56f, h * 0.06f, w * 0.49f, h * 0.15f)
            cubicTo(w * 0.44f, h * 0.22f, w * 0.52f, h * 0.22f, w * 0.52f, h * 0.22f)
            close()
        }
        drawPath(hoja, color = Color(0xFF1E2421))
    }
}
