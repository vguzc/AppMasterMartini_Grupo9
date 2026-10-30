package com.example.appmastermartini_grupo9.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.appmastermartini_grupo9.navigation.Screen
import com.example.appmastermartini_grupo9.ui.theme.MartiniCharcoal
import com.example.appmastermartini_grupo9.ui.theme.MartiniGreen
import com.example.appmastermartini_grupo9.ui.theme.MartiniKhaki

data class FloatingNavItem(
    val route: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * =========================================================================
 * NAVEGACIÓN INFERIOR ESTILO PINTEREST (BOTONES SQUIRCLE INDEPENDIENTES)
 * - Al subir "+" (Crear) y Perfil arriba a la derecha, abajo quedan nuestras
 *   4 secciones principales en cubos redondeados flotantes independientes:
 *   Recetas · Productos · Cursos · Comunidad
 * - Sin texto debajo, dejando ver sutilmente las fotografías entre botones.
 * =========================================================================
 */
@Composable
fun FloatingBottomBar(
    selectedRoute: String,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val baseTileColor = if (isDark) {
        Color(0xFF282B2A).copy(alpha = 0.95f)
    } else {
        Color(0xFFF6F5F0).copy(alpha = 0.96f)
    }

    val activeTileColor = if (isDark) {
        Color(0xFF343A38).copy(alpha = 0.98f)
    } else {
        Color(0xFFFFFFFF)
    }

    val tileShape = RoundedCornerShape(18.dp)

    val items = remember {
        listOf(
            FloatingNavItem(
                route = Screen.Home,
                label = "Recetas",
                selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
                unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook
            ),
            FloatingNavItem(
                route = Screen.Profile,
                label = "Productos",
                selectedIcon = Icons.Filled.GridView,
                unselectedIcon = Icons.Outlined.GridView
            ),
            FloatingNavItem(
                route = Screen.Animaciones,
                label = "Cursos",
                selectedIcon = Icons.Filled.School,
                unselectedIcon = Icons.Outlined.School
            ),
            FloatingNavItem(
                route = Screen.Comunidad,
                label = "Comunidad",
                selectedIcon = Icons.Filled.Groups,
                unselectedIcon = Icons.Outlined.Groups
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 12.dp, top = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selectedRoute == item.route.route

                val tileBg by animateColorAsState(
                    targetValue = if (isSelected) activeTileColor else baseTileColor,
                    animationSpec = tween(200),
                    label = "tileBg"
                )

                val iconTint by animateColorAsState(
                    targetValue = when {
                        isSelected && isDark -> MartiniKhaki
                        isSelected && !isDark -> MartiniGreen
                        isDark -> Color(0xFFB5BCBA)
                        else -> MartiniCharcoal.copy(alpha = 0.65f)
                    },
                    animationSpec = tween(200),
                    label = "iconTint"
                )

                val borderColor = when {
                    isSelected && !isDark -> MartiniGreen.copy(alpha = 0.25f)
                    isDark -> Color.White.copy(alpha = 0.10f)
                    else -> MartiniCharcoal.copy(alpha = 0.08f)
                }

                Surface(
                    shape = tileShape,
                    color = tileBg,
                    modifier = Modifier
                        .size(width = 56.dp, height = 52.dp)
                        .shadow(
                            elevation = if (isSelected) 14.dp else 10.dp,
                            shape = tileShape,
                            ambientColor = Color.Black.copy(alpha = if (isDark) 0.45f else 0.18f),
                            spotColor = Color.Black.copy(alpha = if (isDark) 0.45f else 0.18f)
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = tileShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Tab,
                            onClick = {
                                if (!isSelected) {
                                    onNavigate(item.route)
                                }
                            }
                        )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
