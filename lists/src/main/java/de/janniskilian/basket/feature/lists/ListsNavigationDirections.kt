package de.janniskilian.basket.feature.lists

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.ui.navigation.NavigationDestination
import de.janniskilian.basket.core.util.android.LONG_NOTHING
import de.janniskilian.basket.feature.lists.addlistitem.AddListItemContent
import de.janniskilian.basket.feature.lists.createlist.CreateListContent
import de.janniskilian.basket.feature.lists.list.ListContent
import de.janniskilian.basket.feature.lists.lists.ListsContent

object ListsNavigationDestination : NavigationDestination {

    override val routeScheme get() = "lists"

    @ExperimentalMaterialApi
    @Composable
    override fun Content(arguments: Bundle?) {
        ListsContent()
    }
}

object CreateListNavigationDestination : NavigationDestination {

    const val LIST_ID_PARAM = "listId"

    override val routeScheme get() = "createList?listId={$LIST_ID_PARAM}"

    override val argumentsList
        get() = listOf(navArgument(LIST_ID_PARAM) { type = NavType.LongType })

    fun createRoute(listId: ShoppingListId? = null) =
        "createList?listId=${listId?.value ?: LONG_NOTHING}"

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    override fun Content(arguments: Bundle?) {
        CreateListContent()
    }
}

object ListNavigationDestination : NavigationDestination {

    const val LIST_ID_PARAM = "listId"

    override val routeScheme get() = "list/{$LIST_ID_PARAM}"

    override val argumentsList
        get() = listOf(navArgument(LIST_ID_PARAM) { type = NavType.LongType })

    fun createRoute(listId: ShoppingListId) = "list/${listId.value}"

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    override fun Content(arguments: Bundle?) {
        ListContent()
    }
}

object AddListItemNavigationDestination : NavigationDestination {

    const val LIST_ID_PARAM = "listId"

    override val routeScheme get() = "addListItem/{$LIST_ID_PARAM}"

    override val argumentsList
        get() = listOf(navArgument(LIST_ID_PARAM) { type = NavType.LongType })

    fun createRoute(listId: ShoppingListId) = "addListItem/${listId.value}"

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    override fun Content(arguments: Bundle?) {
        AddListItemContent()
    }
}
