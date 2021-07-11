package de.janniskilian.basket.core.ui.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janniskilian.basket.core.ui.compose.strokeShade

@Composable
fun BasketButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        border = BorderStroke(2.dp, MaterialTheme.colors.strokeShade),
        elevation = null,
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}
