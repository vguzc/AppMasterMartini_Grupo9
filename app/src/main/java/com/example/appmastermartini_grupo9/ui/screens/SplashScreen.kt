package com.example.appmastermartini_grupo9.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.ui.theme.AppMasterMartini_Grupo9Theme
import com.example.appmastermartini_grupo9.ui.theme.MartiniDarkKhaki
import com.example.appmastermartini_grupo9.ui.theme.MartiniFloralWhite
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * =========================================================================
 * PANTALLA DE INICIO MINIMALISTA (SPLASH SCREEN) - MASTER MARTINI CHILE
 * Diseño limpio sobre fondo claro cálido (#F8F8F2) donde el logo oficial
 * sin bandera (logo_nb) es el único protagonista, acompañado de una
 * transición suave de escala, opacidad y una fina línea dorada.
 * =========================================================================
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {}
) {
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.92f) }
    val lineScaleX = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(12f) }

    LaunchedEffect(Unit) {
        // 1. Aparición suave y zoom sutil del logo
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing)
            )
        }

        // 2. Expansión minimalista de la línea dorada y subtítulo "PROFESSIONAL"
        delay(300)
        launch {
            lineScaleX.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }
        launch {
            textOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }

        // 3. Pausa breve y salida limpia hacia el Home
        delay(1400)
        launch {
            logoAlpha.animateTo(0f, tween(300))
        }
        launch {
            textAlpha.animateTo(0f, tween(300))
        }
        launch {
            lineScaleX.animateTo(0f, tween(300))
        }
        delay(300)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo oficial Master Martini (sin bandera inferior)
            Image(
                painter = painterResource(id = R.drawable.logo_nb),
                contentDescription = "Logo Master Martini",
                modifier = Modifier
                    .width(210.dp)
                    .height(125.dp)
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Fina línea dorada minimalista (#B69F58)
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(1.5.dp)
                    .graphicsLayer {
                        scaleX = lineScaleX.value
                        alpha = textAlpha.value
                    }
                    .background(MartiniDarkKhaki)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Sello minimalista "PROFESSIONAL" en verde institucional (#004D42) con Montserrat
            Text(
                text = "PROFESSIONAL",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 12.sp,
                    letterSpacing = 5.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MartiniGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = textAlpha.value
                        translationY = textOffsetY.value
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    AppMasterMartini_Grupo9Theme {
        SplashScreen()
    }
}
