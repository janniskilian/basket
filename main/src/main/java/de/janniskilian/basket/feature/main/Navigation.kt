package de.janniskilian.basket.feature.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navArgument
import de.janniskilian.basket.core.ui.navigation.NavigationDestination
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.feature.articles.ArticleNavigationDestination
import de.janniskilian.basket.feature.categories.CategoryNavigationDestination
import de.janniskilian.basket.feature.lists.AddListItemNavigationDestination
import de.janniskilian.basket.feature.lists.CreateListNavigationDestination
import de.janniskilian.basket.feature.lists.ListNavigationDestination
import de.janniskilian.basket.feature.lists.ListsNavigationDestination
import de.janniskilian.basket.feature.lists.list.ListMoreMenuContent
import de.janniskilian.basket.feature.lists.list.itemorder.ListItemOrderContent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun Navigation(
    startDestination: NavigationDestination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = LocalNavController.current,
        startDestination = ListsNavigationDestination.routeScheme,
        modifier = modifier
    ) {
        NavigationRoot
            .values()
            .forEach { navigationRoot ->
                composable(navigationRoot.destination.routeScheme) {
                    navigationRoot.destination.Content()
                }
            }

        addDestination(OnboardingDestination)
        addDestination(CreateListNavigationDestination)
        addDestination(ListNavigationDestination)
        addDestination(AddListItemNavigationDestination)
        addDestination(ArticleNavigationDestination)
        addDestination(CategoryNavigationDestination)

        dialog(
            "list/itemOrder/{listId}",
            listOf(navArgument(ListNavigationDestination.LIST_ID_PARAM) {
                type = NavType.LongType
            }),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            ListItemOrderContent()
        }

        dialog(
            "list/moreMenu/{listId}",
            listOf(navArgument(ListNavigationDestination.LIST_ID_PARAM) {
                type = NavType.LongType
            }),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            ListMoreMenuContent()
        }
    }
}

private fun NavGraphBuilder.addDestination(destination: NavigationDestination) {
    composable(destination.routeScheme, destination.argumentsList) {
        destination.Content()
    }
}
