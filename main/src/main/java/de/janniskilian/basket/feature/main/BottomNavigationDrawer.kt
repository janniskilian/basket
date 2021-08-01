package de.janniskilian.basket.feature.main

import androidx.activity.compose.BackHandler
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.BottomDrawerColumn
import de.janniskilian.basket.core.ui.compose.component.BottomDrawerItem
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

    BottomNavigationDrawerLayout(
        currentRoute = currentRoute,
        onItemClick = {
            coroutineScope.launch {
                bottomDrawerState.close()
            }

            onItemClick(it)
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun BottomNavigationDrawerLayout(
    currentRoute: String?,
    onItemClick: (navigationRoot: NavigationRoot) -> Unit
) {
    BottomDrawerColumn {
        NavigationRoot
            .values()
            .forEach {
                BottomDrawerItem(
                    it.icon,
                    stringResource(it.nameResId),
                    it.destination.routeScheme == currentRoute
                ) {
                    onItemClick(it)
                }
            }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun BottomNavigationDrawerPreview() {
    BasketTheme {
        BottomNavigationDrawerLayout(
            currentRoute = NavigationRoot.ARTICLES.name,
            onItemClick = {}
        )
    }
}
