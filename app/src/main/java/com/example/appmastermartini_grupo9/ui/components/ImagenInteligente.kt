package com.example.appmastermartini_grupo9.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Componente reutilizable ImagenInteligente (Guía 13).
 * Muestra la imagen de perfil en forma circular.
 * Si no hay imagen seleccionada o capturada, muestra un icono por defecto.
 */
@Composable
fun ImagenInteligente(
    imagenUri: Uri?,
    modifier: Modifier = Modifier,
    tamano: Dp = 160.dp
) {
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (imagenUri != null) {
            // Carga y renderizado de la imagen desde el Uri nativo
            AsyncImage(
                model = imagenUri,
                contentDescription = "Foto de Perfil Master Martini",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Ícono de perfil por defecto si no hay imagen
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Sin imagen de perfil",
                modifier = Modifier.size(tamano * 0.55f),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
