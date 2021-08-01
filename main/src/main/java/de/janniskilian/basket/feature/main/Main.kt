package de.janniskilian.basket.feature.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janniskilian.basket.core.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun Main(
    bottomDrawerState: BottomDrawerState,
    startDestination: NavigationDestination,
    currentRoute: String?,
    onBottomNavigationDrawerItemClick: (navigationRoot: NavigationRoot) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    BottomDrawer(
        gesturesEnabled = false,
        drawerState = bottomDrawerState,
        drawerShape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        drawerContent = {
            BottomNavigationDrawer(
                currentRoute = currentRoute,
                bottomDrawerState = bottomDrawerState,
                onItemClick = { onBottomNavigationDrawerItemClick(it) }
            )
        },
        content = {
            BasketScaffold(
                onHomeButtonClick = {
                    coroutineScope.launch {
                        bottomDrawerState.expand()
                    }
                }
            ) {
                Navigation(
                    startDestination = startDestination,
                    modifier = Modifier
                        .padding(bottom = it.calculateBottomPadding())
                        .contentImePadding(it.calculateBottomPadding())
                )
            }
        }
    )
}
