package com.example.appmastermartini_grupo9.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MartiniGreenLight,
    onPrimary = MartiniGreenDark,
    primaryContainer = MartiniGreen,
    onPrimaryContainer = MartiniWhite,
    secondary = MartiniDarkKhaki,
    onSecondary = MartiniGreenDark,
    secondaryContainer = MartiniSaddleBrown,
    onSecondaryContainer = MartiniKhaki,
    tertiary = MartiniKhaki,
    onTertiary = MartiniGreenDark,
    background = MartiniGreenDark,
    surface = Color(0xFF141E1C),
    surfaceVariant = MartiniSlate,
    onBackground = MartiniWhite,
    onSurface = MartiniWhite,
    onSurfaceVariant = MartiniGreenContainer,
    error = MartiniCrimson,
    onError = MartiniWhite
)

private val LightColorScheme = lightColorScheme(
    primary = MartiniGreen,                   // #004D42 Verde principal Master Martini
    onPrimary = MartiniWhite,                 // #FFFFFF
    primaryContainer = MartiniGreenContainer, // #CFD7CB Salvia suave
    onPrimaryContainer = MartiniGreenDark,    // #001311
    secondary = MartiniDarkKhaki,             // #B69F58 Dorado institucional
    onSecondary = MartiniWhite,               // #FFFFFF
    secondaryContainer = MartiniKhaki,        // #FDF69E Khaki luminoso
    onSecondaryContainer = MartiniSaddleBrown,// #3B2F21 Cacao
    tertiary = MartiniSaddleBrown,            // #3B2F21
    onTertiary = MartiniWhite,
    background = MartiniFloralWhite,          // #F8F8F2 Blanco cálido
    surface = MartiniWhite,                   // #FFFFFF
    surfaceVariant = MartiniWhiteSmoke,       // #F2F3F5
    onBackground = MartiniCharcoal,           // #212326
    onSurface = MartiniCharcoal,              // #212326
    onSurfaceVariant = MartiniSlate,          // #304148
    error = MartiniCrimson,                   // #E0284F
    onError = MartiniWhite
)

@Composable
fun AppMasterMartini_Grupo9Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado por defecto para preservar identidad de marca Master Martini
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
