package de.janniskilian.basket.feature.categories

import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.core.ui.navigation.NavigationDestination
import de.janniskilian.basket.feature.categories.categories.CategoriesContent
import de.janniskilian.basket.feature.categories.category.CategoryContent

object CategoriesNavigationDestination : NavigationDestination {

    override val routeScheme get() = "categories"

    @ExperimentalMaterialApi
    @Composable
    override fun Content(arguments: Bundle?) {
        CategoriesContent()
    }
}

object CategoryNavigationDestination : NavigationDestination {

    const val CATEGORY_ID_PARAM = "categoryId"

    override val routeScheme get() = "category/{$CATEGORY_ID_PARAM}"

    override val argumentsList
        get() = listOf(navArgument(CATEGORY_ID_PARAM) { type = NavType.LongType })

    fun createRoute(categoryId: CategoryId? = null) = "category/${categoryId?.value}"

    @Composable
    override fun Content(arguments: Bundle?) {
        CategoryContent()
    }
}
