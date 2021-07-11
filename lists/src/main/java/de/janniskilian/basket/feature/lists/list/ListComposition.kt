package de.janniskilian.basket.feature.lists.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListItem
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.EmptyScreen
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.core.util.test.createTestArticle
import de.janniskilian.basket.core.util.test.createTestCategory
import de.janniskilian.basket.core.util.test.createTestShoppingList
import de.janniskilian.basket.core.util.test.createTestShoppingListItem
import de.janniskilian.basket.feature.lists.AddListItemNavigationDestination
import de.janniskilian.basket.feature.lists.R

private const val NO_CATEGORY_ID = -1L
private const val CHECKED_CATEGORY_ID = -2L

private const val KEY_PREFIX_CATEGORY = "CATEGORY_"
private const val KEY_PREFIX_LIST_ITEM = "LIST_ITEM_"


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ListContent(
    viewModel: ListViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val shoppingList by viewModel.shoppingList.collectAsState()

    ListLayout(
        shoppingList = shoppingList,
        onItemClick = viewModel::listItemClicked,
        onFabClick = {
            shoppingList?.id?.let {
                navController.navigate(AddListItemNavigationDestination.createRoute(it))
            }
        }
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun ListLayout(
    shoppingList: ShoppingList?,
    onItemClick: (item: ShoppingListItem) -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.add_list_item_icon_desc)
                )
            }
        }
    ) {
        Crossfade(
            targetState = shoppingList?.isEmpty,
            modifier = Modifier.fillMaxWidth()
        ) { isListEmpty ->
            when (isListEmpty) {
                true -> ListEmptyState()
                false, null -> ListNotEmptyState(shoppingList, onItemClick)
            }
        }
    }
}

@Composable
private fun ListEmptyState() {
    EmptyScreen(
        imageVector = Icons.Outlined.SentimentSatisfied,
        imageDescription = stringResource(R.string.shopping_list_empty_image_desc),
        headline = stringResource(R.string.shopping_list_empty_title),
        info = stringResource(R.string.shopping_list_empty_info)
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun ListNotEmptyState(
    shoppingList: ShoppingList?,
    onItemClick: (item: ShoppingListItem) -> Unit
) {
    val (checkedItems, uncheckedItems) = shoppingList
        ?.items
        .orEmpty()
        .partition { it.isChecked }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 10.du),
        modifier = Modifier.fillMaxSize()
    ) {
        uncheckedItems(
            items = uncheckedItems,
            isGroupedByCategory = shoppingList?.isGroupedByCategory ?: true,
            onItemClick = onItemClick
        )

        checkedItems(
            items = checkedItems,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun ListCategoryHeader(category: Category?) {
    Text(
        text = category?.name ?: stringResource(R.string.category_default),
        style = MaterialTheme.typography.body2,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(
                horizontal = 2.du,
                vertical = 1.du
            )
    )
}

@ExperimentalMaterialApi
@Composable
private fun ListItem(
    shoppingListItem: ShoppingListItem,
    onClick: () -> Unit
) {
    androidx.compose.material.ListItem(
        icon = {
            Checkbox(
                checked = shoppingListItem.isChecked,
                onCheckedChange = { onClick() }
            )
        },
        text = {
            Text(
                text = shoppingListItem.article.name,
                style = MaterialTheme.typography.body1
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )

    Divider(startIndent = 9.du)
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
private fun LazyListScope.uncheckedItems(
    items: List<ShoppingListItem>,
    isGroupedByCategory: Boolean,
    onItemClick: (item: ShoppingListItem) -> Unit
) {
    if (isGroupedByCategory) {
        val uncheckedItemGroups = items.groupBy { it.article.category }
        val categories = uncheckedItemGroups.keys.sortedBy { it?.name }

        categories.forEach { category ->
            stickyHeader(key = KEY_PREFIX_CATEGORY + (category?.id?.value ?: NO_CATEGORY_ID)) {
                ListCategoryHeader(category)
            }

            listItems(
                items = uncheckedItemGroups[category]
                    .orEmpty()
                    .sortedBy(::selectArticleName),
                onItemClick = onItemClick
            )

            item {
                Spacer(modifier = Modifier.size(4.du))
            }
        }
    } else {
        listItems(
            items = items,
            onItemClick = onItemClick
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
private fun LazyListScope.checkedItems(
    items: List<ShoppingListItem>,
    onItemClick: (item: ShoppingListItem) -> Unit
) {
    val checkedListItems = items.sortedBy { it.article.name }
    if (checkedListItems.isNotEmpty()) {
        stickyHeader(key = KEY_PREFIX_CATEGORY + CHECKED_CATEGORY_ID) {
            val checkedGroupName = stringResource(R.string.checked_items_group)
            val checkedCategory = remember {
                Category(
                    CategoryId(CHECKED_CATEGORY_ID),
                    checkedGroupName
                )
            }
            ListCategoryHeader(checkedCategory)
        }

        listItems(
            items = checkedListItems.sortedBy(::selectArticleName),
            onItemClick = onItemClick
        )

        item {
            Spacer(modifier = Modifier.size(4.du))
        }
    }
}

@ExperimentalMaterialApi
private fun LazyListScope.listItems(
    items: List<ShoppingListItem>,
    onItemClick: (item: ShoppingListItem) -> Unit
) {
    items(items, key = { KEY_PREFIX_LIST_ITEM + it.id.value }) {
        ListItem(
            shoppingListItem = it,
            onClick = { onItemClick(it) })
    }
}

private fun selectArticleName(item: ShoppingListItem): String =
    item.article.name

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun ListLayoutPreview() {
    val categories = (0 until 5).map { createTestCategory() }
    val articles = (0 until 20).map { createTestArticle(categories.random()) }

    var list = createTestShoppingList()
    val listItems = (0 until 25).map {
        createTestShoppingListItem(list, articles.random())
    }
    list = createTestShoppingList(listItems)

    BasketTheme {
        ListLayout(
            shoppingList = list
        )
    }
}
