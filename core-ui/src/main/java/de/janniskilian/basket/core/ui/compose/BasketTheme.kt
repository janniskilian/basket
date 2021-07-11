package de.janniskilian.basket.core.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import de.janniskilian.basket.core.ui.R

@Composable
fun BasketTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) {
        BasketColors.dark()
    } else {
        BasketColors.light()
    }

    val typography = Typography(
        h1 = TextStyle(
            fontFamily = FontFamily(Font(R.font.roboto_slab_700)),
            fontSize = 26.sp,
            letterSpacing = 0.025.sp
        ),
        h2 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 22.sp,
            letterSpacing = 0.02.sp
        )
    )

    val shapes = Shapes(
        small = RoundedCornerShape(1.du),
        medium = RoundedCornerShape(2.du),
        large = RoundedCornerShape(3.du)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
