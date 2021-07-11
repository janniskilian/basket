package de.janniskilian.basket.core.ui.compose

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Colors.colorControlNormal
    get() = if (isLight) {
        onSurface.copy(alpha = 0.6f)
    } else {
        onSurface.copy(alpha = 0.87f)
    }

val Colors.textColorPrimary
    get() = if (isLight) {
        onSurface.copy(alpha = 0.87f)
    } else {
        onSurface
    }

val Colors.textColorSecondary
    get() = if (isLight) {
        onSurface.copy(alpha = 0.6f)
    } else {
        onSurface.copy(alpha = 0.87f)
    }

val Colors.textColorTertiary
    get() = if (isLight) {
        onSurface.copy(alpha = 0.38f)
    } else {
        onSurface.copy(alpha = 0.6f)
    }

val Colors.onSurfaceShade
    get() = onSurface.copy(alpha = 0.12f)

val Colors.primaryShade
    get() = primary.copy(alpha = 0.12f)

val Colors.strokeShade
    get() = Color(0x33000000)
