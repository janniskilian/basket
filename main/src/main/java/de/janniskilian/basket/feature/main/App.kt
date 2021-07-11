package de.janniskilian.basket.feature.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import de.janniskilian.basket.R
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.util.KEY_DEFAULT_DATA_IMPORTED
import de.janniskilian.basket.core.util.android.defaultPreferencesDataStore
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.feature.lists.AddListItemNavigationDestination
import de.janniskilian.basket.feature.lists.CreateListNavigationDestination
import de.janniskilian.basket.feature.lists.ListNavigationDestination
import de.janniskilian.basket.feature.lists.ListsNavigationDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
@ExperimentalAnimationApi
@ExperimentalMaterialApi
fun App() {
    val navController = rememberNavController()
    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val currentRoute by getCurrentRouteState(navController)

    val context = LocalContext.current
    val preferencesDataStore = remember { context.defaultPreferencesDataStore }

    val isDefaultDataImported = remember {
        runBlocking {
            preferencesDataStore
                .data
                .first()[KEY_DEFAULT_DATA_IMPORTED]
        }
    }

    val startDestination = if (isDefaultDataImported != true) {
        OnboardingDestination
    } else {
        ListsNavigationDestination
    }

    BasketTheme {
        ProvideWindowInsets {
            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                Main(
                    bottomDrawerState = bottomDrawerState,
                    startDestination = startDestination,
                    currentRoute = currentRoute,
                    onBottomNavigationDrawerItemClick = {
                        navController.onBottomNavigationDrawerItemClick(it)
                    }
                )
            }
        }
    }
}

private fun NavHostController.onBottomNavigationDrawerItemClick(navigationRoot: NavigationRoot) {
    val currentRoute = currentDestination?.route

    navigate(navigationRoot.destination.routeScheme) {
        if (currentRoute != null) {
            popUpTo(currentRoute) {
                inclusive = true
            }
        }
    }
}

@Composable
private fun getCurrentRouteState(navController: NavHostController): MutableState<String?> {
    val currentRoute = remember { mutableStateOf(navController.currentDestination?.route) }

    navController.addOnDestinationChangedListener { _, _, _ ->
        currentRoute.value = navController.currentDestination?.route
    }

    return currentRoute
}
