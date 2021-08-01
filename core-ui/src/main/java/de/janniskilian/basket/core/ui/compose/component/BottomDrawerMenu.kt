package de.janniskilian.basket.core.ui.compose.component

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@ExperimentalMaterialApi
@Composable
fun <T : BottomDrawerMenuItem> BottomDrawerMenu(
    items: List<T>,
    onItemClick: (item: T) -> Unit = {}
) {
    BottomDrawerColumn {
        items.forEach {
            BottomDrawerItem(
                icon = it.icon(),
                text = it.text(),
                isSelected = false,
                onClick = { onItemClick(it) }
            )
        }
    }
}

interface BottomDrawerMenuItem {

    @Composable
    fun icon(): ImageVector

    @Composable
    fun text(): String
}
