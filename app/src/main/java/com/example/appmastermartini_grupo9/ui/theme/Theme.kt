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
    primary = MartiniNavyLight,
    onPrimary = MartiniNavyDark,
    primaryContainer = MartiniNavy,
    onPrimaryContainer = Color.White,
    secondary = MartiniGoldLight,
    onSecondary = Color.Black,
    tertiary = MartiniLightBlue,
    background = Color(0xFF121316),
    surface = Color(0xFF1E1F24),
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MartiniNavy,
    onPrimary = Color.White,
    primaryContainer = MartiniLightBlue,
    onPrimaryContainer = MartiniNavyDark,
    secondary = MartiniGold,
    onSecondary = Color.White,
    tertiary = MartiniBlue,
    background = MartiniCream,
    surface = Color.White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
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
