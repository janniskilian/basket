package de.janniskilian.basket.feature.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ButtonColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.colorControlNormal
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.ui.compose.primaryShade
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun BottomNavigationDrawer(
    currentRoute: String?,
    bottomDrawerState: BottomDrawerState,
    onItemClick: (navigationRoot: NavigationRoot) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    BackHandler(bottomDrawerState.isExpanded) {
        coroutineScope.launch {
            bottomDrawerState.close()
        }
    }

    BottomNavigationDrawerColumn(currentRoute) {
        coroutineScope.launch {
            bottomDrawerState.close()
        }

        onItemClick(it)
    }
}

@ExperimentalMaterialApi
@Composable
private fun BottomNavigationDrawerColumn(
    currentRoute: String?,
    onItemClick: (navigationRoot: NavigationRoot) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(1.du),
        modifier = Modifier.padding(
            horizontal = 1.du,
            vertical = 2.du
        )
    ) {
        val navigationRoots = NavigationRoot.values()
        navigationRoots.forEach {
            BottomNavigationDrawerItem(
                it,
                it.destination.routeScheme == currentRoute
            ) {
                onItemClick(it)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun BottomNavigationDrawerItem(
    navigationRoot: NavigationRoot,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        colors = BottomNavigationDrawerItemColor(isSelected),
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
                imageVector = navigationRoot.icon,
                contentDescription = navigationRoot.name,
                tint = iconColor
            )

            Text(
                text = stringResource(navigationRoot.nameResId),
                color = textColor,
                modifier = Modifier.padding(start = 3.du)
            )
        }
    }
}

private class BottomNavigationDrawerItemColor(private val isSelected: Boolean) : ButtonColors {

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

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun BottomNavigationDrawerPreview() {
    BasketTheme {
        BottomNavigationDrawerColumn(
            currentRoute = NavigationRoot.ARTICLES.name,
            onItemClick = {}
        )
    }
}
