package com.example.appmastermartini_grupo9.ui.components

import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

/**
 * =========================================================================
 * REPRODUCTOR FLUIDO DE VIDEO CON ANDROIDX MEDIA3 (EXOPLAYER)
 * Optimizado para recetas con video (pins de video / Don Saco Roto):
 * - Reproducción continua con loop suave.
 * - Control táctil: Pausa/Play con indicador animado.
 * - Botón flotante para silenciar o activar audio.
 * - Liberación automática de memoria al salir de la pantalla.
 * =========================================================================
 */
@OptIn(UnstableApi::class)
@Composable
fun MartiniVideoPlayer(
    modifier: Modifier = Modifier,
    @RawRes videoRes: Int = 0,
    videoUri: Uri? = null,
    autoPlay: Boolean = true,
    initialMuted: Boolean = false,
    contentDescription: String = "Video de receta"
) {
    val context = LocalContext.current

    val mediaUri: Uri? = remember(videoRes, videoUri) {
        when {
            videoUri != null -> videoUri
            videoRes != 0 -> RawResourceDataSource.buildRawResourceUri(videoRes)
            else -> null
        }
    }

    if (mediaUri == null) return

    var isPlaying by remember { mutableStateOf(autoPlay) }
    var isMuted by remember { mutableStateOf(initialMuted) }
    var showPlayPauseIndicator by remember { mutableStateOf(false) }

    val exoPlayer = remember(context, mediaUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(mediaUri))
            prepare()
            playWhenReady = autoPlay
            repeatMode = Player.REPEAT_MODE_ALL
            volume = if (initialMuted) 0f else 1f
        }
    }

    // Efecto para ocultar el indicador central de play/pause después de 700ms
    LaunchedEffect(showPlayPauseIndicator) {
        if (showPlayPauseIndicator) {
            delay(700)
            showPlayPauseIndicator = false
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, exoPlayer) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                exoPlayer.pause()
                isPlaying = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                    isPlaying = false
                } else {
                    exoPlayer.play()
                    isPlaying = true
                }
                showPlayPauseIndicator = true
            }
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Indicador central animado de Play / Pausa al tocar
        AnimatedVisibility(
            visible = showPlayPauseIndicator,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.55f),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (isPlaying) "Reproduciendo" else "Pausado",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Botón flotante para silenciar o activar audio (esquina superior derecha, bajo la barra de acciones)
        Surface(
            onClick = {
                isMuted = !isMuted
                exoPlayer.volume = if (isMuted) 0f else 1f
            },
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.52f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 64.dp, end = 18.dp)
                .size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = if (isMuted) "Activar audio" else "Silenciar",
                    tint = Color.White,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}
