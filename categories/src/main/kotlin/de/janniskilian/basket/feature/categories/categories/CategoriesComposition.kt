package de.janniskilian.basket.feature.categories.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.core.util.test.createTestCategory
import de.janniskilian.basket.feature.categories.CategoryNavigationDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@ExperimentalMaterialApi
@Composable
fun CategoriesContent(
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    CategoriesLayout(
        categoriesFlow = viewModel.categories,
        categoryClickListener = {
            navController.navigate(
                CategoryNavigationDestination.createRoute(it.id)
            )
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun CategoriesLayout(
    categoriesFlow: Flow<PagingData<Category>>,
    categoryClickListener: (category: Category) -> Unit
) {
    val categoryItems = categoriesFlow.collectAsLazyPagingItems()

    LazyColumn {
        itemsIndexed(categoryItems) { i, category ->
            if (category != null) {
                CategoryItem(category, categoryClickListener)

                if (i < categoryItems.itemCount - 1) {
                    Divider()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CategoryItem(category: Category, articleClickListener: (category: Category) -> Unit) {
    ListItem(
        modifier = Modifier.clickable { articleClickListener(category) }
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.body1
        )
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun CategoriesDefaultPreview() {
    val categories = (0 until NUM_PREVIEW_CATEGORIES).map { createTestCategory() }

    BasketTheme {
        CategoriesLayout(
            categoriesFlow = flowOf(PagingData.from(categories)),
            categoryClickListener = {}
        )
    }
}

private const val NUM_PREVIEW_CATEGORIES = 20
