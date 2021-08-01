package de.janniskilian.basket.core.ui.compose.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import de.janniskilian.basket.core.ui.compose.colorControlNormal
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.ui.compose.primaryShade

@ExperimentalMaterialApi
@Composable
fun BottomDrawerItem(
    icon: ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        colors = BottomDrawerItemColor(isSelected),
        onClick = { if (!isSelected) onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val iconColor = if (isSelected) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.colorControlNormal
            }
            val textColor = if (isSelected) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.onSurface
            }

            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconColor
            )

            Text(
                text = text,
                color = textColor,
                modifier = Modifier.padding(start = 3.du)
            )
        }
    }
}

private class BottomDrawerItemColor(private val isSelected: Boolean) : ButtonColors {

    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(
            if (isSelected) {
                MaterialTheme.colors.primaryShade
            } else {
                Color.Transparent
            }
        )

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(MaterialTheme.colors.primary)
}
