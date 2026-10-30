package com.example.appmastermartini_grupo9.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appmastermartini_grupo9.R
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenContainer
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreenDark

/**
 * =========================================================================
 * ENCABEZADO ESTILO PINTEREST CON IDENTIDAD MASTER MARTINI
 * - Sin texto superior ("MASTER MARTINI"): solo el título limpio a la izquierda
 *   ("Descubre", "Productos", "Aprende", "Comunidad", "Mi espacio").
 * - Derecha: botón "+" (Crear receta) + botón de Perfil (en la posición del
 *   globo de chat de Pinterest).
 * =========================================================================
 */
@Composable
fun MartiniTopHeader(
    title: String,
    onCreateClick: (() -> Unit)? = null,
    avatarUri: Uri? = null,
    onAvatarClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (onBackClick != null) {
                Surface(
                    onClick = onBackClick,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 27.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono "+" limpio arriba a la derecha (estilo Pinterest)
            if (onCreateClick != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable(
                            role = Role.Button,
                            onClick = onCreateClick
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear receta",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Avatar de Perfil unificado con el mismo monito/foto de la sección de Usuario
            if (onAvatarClick != null) {
                MartiniProfileAvatar(
                    avatarUri = avatarUri,
                    size = 36.dp,
                    modifier = Modifier.clickable(
                        role = Role.Button,
                        onClick = onAvatarClick
                    )
                )
            }
        }
    }
}

/**
 * Avatar circular unificado en toda la app:
 * Muestra la foto seleccionada por el usuario (AsyncImage) o un elegante monograma/icono
 * con la identidad visual Master Martini cuando no hay foto cargada.
 */
@Composable
fun MartiniProfileAvatar(
    avatarUri: Uri?,
    size: Dp = 36.dp,
    nombreUsuario: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MartiniGreenDark)
    ) {
        if (avatarUri != null) {
            AsyncImage(
                model = avatarUri,
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            val letra = nombreUsuario?.trim()?.firstOrNull()?.uppercaseChar()?.toString()
            if (letra != null) {
                Text(
                    text = letra,
                    color = Color.White,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    fontSize = (size.value * 0.45f).sp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.58f)
                )
            }
        }
    }
}

/**
 * =========================================================================
 * BARRA HORIZONTAL ESTILO PINTEREST ("Todos" CON SUBRAYADO INFERIOR)
 * Incluye ícono discreto de búsqueda contextual + pestañas de texto limpias
 * con línea inferior activa en vez de píldoras pesadas.
 * =========================================================================
 */
@Composable
fun PinterestCategoryTabs(
    tabs: List<String>,
    selectedTab: String,
    onSelectTab: (String) -> Unit,
    searchQuery: String = "",
    onSearchQueryChange: ((String) -> Unit)? = null,
    searchPlaceholder: String = "Buscar...",
    searchVisible: Boolean = false,
    onToggleSearch: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onToggleSearch != null) {
                val searchActive = searchVisible || searchQuery.isNotBlank()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            if (searchActive) MartiniGreen.copy(alpha = 0.14f) else Color.Transparent
                        )
                        .clickable(onClick = onToggleSearch)
                ) {
                    Icon(
                        imageVector = if (searchVisible) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = if (searchActive) {
                            MartiniGreen
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.78f)
                        },
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            tabs.forEach { tab ->
                val isSelected = selectedTab == tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Tab,
                            onClick = { onSelectTab(tab) }
                        )
                ) {
                    Text(
                        text = tab,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = (-0.1).sp
                        ),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isSelected) {
                                    MaterialTheme.colorScheme.onBackground
                                } else {
                                    Color.Transparent
                                }
                            )
                    )
                }
            }
        }

        if (onToggleSearch != null && onSearchQueryChange != null) {
            AnimatedVisibility(
                visible = searchVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = searchPlaceholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MartiniGreen
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar búsqueda",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MartiniGreen
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
            }
        }
    }
}
