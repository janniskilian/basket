package de.janniskilian.basket.feature.groups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.EmptyScreen
import de.janniskilian.feature.groups.R

@Composable
fun GroupsContent() {
    GroupsLayout()
}

@Composable
private fun GroupsLayout() {
    EmptyScreen(
        imageVector = Icons.Outlined.GroupWork,
        imageDescription = stringResource(R.string.groups_lead_image_desc),
        headline = stringResource(R.string.groups_empty_title),
        info = stringResource(R.string.groups_empty_info)
    )
}

@Preview(showBackground = true)
@Composable
private fun GroupsLayoutPreview() {
    BasketTheme {
        GroupsContent()
    }
}
