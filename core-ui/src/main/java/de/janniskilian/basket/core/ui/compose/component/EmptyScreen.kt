package de.janniskilian.basket.core.ui.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.ui.compose.onSurfaceShade
import de.janniskilian.basket.core.ui.compose.textColorPrimary
import de.janniskilian.basket.core.ui.compose.textColorSecondary

@Composable
fun EmptyScreen(
    imageVector: ImageVector,
    imageDescription: String,
    headline: String,
    info: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(2.du)
    ) {
        EmptyScreenImage(imageVector, imageDescription)
        EmptyScreenHeadline(headline, modifier = Modifier.padding(top = 3.du))
        EmptyScreenInfo(info, modifier = Modifier.padding(top = 1.du))
    }
}

@Composable
private fun EmptyScreenImage(
    imageVector: ImageVector,
    imageDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        imageVector = imageVector,
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurfaceShade),
        contentDescription = imageDescription,
        modifier = Modifier
            .size(12.du)
            .then(modifier)
    )
}

@Composable
private fun EmptyScreenHeadline(
    headline: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = headline,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.textColorPrimary,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun EmptyScreenInfo(
    info: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = info,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.textColorSecondary,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyScreenPreview() {
    BasketTheme {
        EmptyScreen(
            imageVector = Icons.Outlined.GroupWork,
            imageDescription = "Icon that symbolises groups",
            headline = "You have no groups",
            info = """A group can contain multiple articles that you regularly buy at the same time.
                 You can then add the whole group to a list in one go.""".trimIndent()
        )
    }
}
