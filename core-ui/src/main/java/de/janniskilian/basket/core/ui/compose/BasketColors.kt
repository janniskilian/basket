package de.janniskilian.basket.core.ui.compose

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Suppress("MagicNumber")
object BasketColors {

    private const val SHADE_PRIMARY_ALPHA = 0.87f

    val brandGreen = Color(0xFF388E3C)
    val brandGreenDark = Color(0xFF00600F)
    val brandYellow = Color(0xFFFFC400)
    val brandYellowDark = Color(0xFFC79400)

    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
    val transparent = Color(0x00000000)

    val blackShadePrimary = black.copy(alpha = SHADE_PRIMARY_ALPHA)

    val shadePrimary = Color(0x1F388E3C)
    val divider = Color(0x1F000000)

    val orange = Color(0xFFE65100)
    val red = Color(0xFFB71C1C)
    val pink = Color(0xFF880E4F)
    val purple = Color(0xFF4A148C)
    val indigo = Color(0xFF1A237E)
    val cyan = Color(0xFF006064)
    val green = Color(0xFF1B5E20)
    val blueGrey = Color(0xFF263238)
    val brown = Color(0xFF3E2723)
    val grey = Color(0xFF212121)

    val listColors
        get() = listOf(
            orange,
            red,
            pink,
            purple,
            indigo,
            cyan,
            green,
            blueGrey,
            brown,
            grey
        )

    private val primary = brandGreen
    private val primaryVariant = brandGreenDark
    private val secondary = brandYellow
    private val secondaryVariant = brandYellowDark

    @Composable
    fun light() = lightColors(
        primary = primary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        secondaryVariant = secondaryVariant,
        onPrimary = Color.White,
        onSecondary = blackShadePrimary,
        onBackground = blackShadePrimary,
        onSurface = blackShadePrimary
    )

    @Composable
    fun dark() = darkColors(
        primary = primary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        secondaryVariant = secondaryVariant,
        onPrimary = Color.White,
        onSecondary = blackShadePrimary
    )
}
