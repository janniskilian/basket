package de.janniskilian.basket.feature.lists.lists

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.colorControlNormal
import de.janniskilian.basket.core.ui.compose.component.EmptyScreen
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.ui.compose.strokeShade
import de.janniskilian.basket.core.ui.compose.textColorPrimary
import de.janniskilian.basket.core.ui.compose.textColorSecondary
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.core.util.test.createTestArticle
import de.janniskilian.basket.core.util.test.createTestCategory
import de.janniskilian.basket.core.util.test.createTestShoppingList
import de.janniskilian.basket.core.util.test.createTestShoppingListItem
import de.janniskilian.basket.feature.lists.CreateListNavigationDestination
import de.janniskilian.basket.feature.lists.ListNavigationDestination
import de.janniskilian.basket.feature.lists.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@ExperimentalMaterialApi
@Composable
fun ListsContent(viewModel: ListsViewModel = hiltViewModel()) {
    val navController = LocalNavController.current

    val listsFlow = viewModel.shoppingLists
    val isEmpty by viewModel.isEmpty.collectAsState()

    ListsLayout(
        listsFlow = listsFlow,
        isEmpty = isEmpty,
        onListClick = { navController.navigate(ListNavigationDestination.createRoute(it.id)) },
        onFabClick = { navController.navigate(CreateListNavigationDestination.createRoute()) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun ListsLayout(
    listsFlow: Flow<PagingData<ShoppingList>>,
    isEmpty: Boolean,
    onListClick: (list: ShoppingList) -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = stringResource(R.string.create_list_icon_desc)
                )
            }
        }
    ) {
        Crossfade(targetState = isEmpty) { isEmpty ->
            when (isEmpty) {
                true -> ListsEmptyState()
                false -> ListsColumn(
                    listsFlow = listsFlow,
                    listClickListener = onListClick
                )
            }
        }
    }
}

@Composable
private fun ListsEmptyState() {
    EmptyScreen(
        imageVector = Icons.Outlined.ListAlt,
        imageDescription = stringResource(R.string.shopping_lists_lead_image_desc),
        headline = stringResource(R.string.shopping_lists_empty_title),
        info = stringResource(R.string.shopping_lists_empty_info)
    )
}

@ExperimentalMaterialApi
@Composable
private fun ListsColumn(
    listsFlow: Flow<PagingData<ShoppingList>>,
    listClickListener: (ShoppingList) -> Unit = {}
) {
    BasketTheme(isDarkTheme = true) {
        val listItems = listsFlow.collectAsLazyPagingItems()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.du),
            contentPadding = PaddingValues(2.du)
        ) {
            items(listItems) { list ->
                if (list != null) {
                    ListItem(list, listClickListener)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ListItem(list: ShoppingList, listClickListener: (list: ShoppingList) -> Unit = {}) {
    Card(
        backgroundColor = Color(list.color),
        border = BorderStroke(2.dp, MaterialTheme.colors.strokeShade),
        elevation = 0.5.du,
        onClick = { listClickListener(list) },
        modifier = Modifier.fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (nameRef, itemsTotalRef, itemsCheckedRef, moreButtonRef) = createRefs()

            Text(
                text = list.name,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.textColorPrimary,
                modifier = Modifier.constrainAs(nameRef) {
                    width = Dimension.fillToConstraints

                    linkTo(
                        start = parent.start,
                        end = moreButtonRef.start,
                        startMargin = 3.du,
                        endMargin = 1.du
                    )
                    top.linkTo(parent.top, 3.du)
                }
            )

            Text(
                text = LocalContext.current.resources.getQuantityString(
                    R.plurals.shopping_list_items_total,
                    list.items.size,
                    list.items.size
                ),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.textColorSecondary,
                modifier = Modifier.constrainAs(itemsTotalRef) {
                    width = Dimension.fillToConstraints

                    linkTo(
                        start = nameRef.start,
                        end = nameRef.end
                    )
                    top.linkTo(nameRef.bottom, 2.du)
                }
            )

            Text(
                text = LocalContext.current.resources.getQuantityString(
                    R.plurals.shopping_list_items_checked,
                    list.checkedItemCount,
                    list.checkedItemCount
                ),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.textColorSecondary,
                modifier = Modifier.constrainAs(itemsCheckedRef) {
                    width = Dimension.fillToConstraints

                    linkTo(
                        start = nameRef.start,
                        end = nameRef.end
                    )
                    top.linkTo(itemsTotalRef.bottom, 0.5.du)
                    bottom.linkTo(parent.bottom, 3.du)
                }
            )

            IconButton(
                onClick = { },
                modifier = Modifier.constrainAs(moreButtonRef) {
                    width = Dimension.preferredValue(24.dp)
                    height = Dimension.preferredValue(24.dp)

                    top.linkTo(parent.top, 1.du)
                    end.linkTo(parent.end, 1.du)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = stringResource(R.string.options_menu_button_desc),
                    tint = MaterialTheme.colors.colorControlNormal
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun ListsLayoutPreview() {
    val categories = (0 until 5).map { createTestCategory() }
    val articles = (0 until 20).map { createTestArticle(categories.random()) }

    val list = createTestShoppingList()

    val lists = (0 until 5).map {
        val listItems = (0 until 25).map {
            createTestShoppingListItem(list, articles.random())
        }

        createTestShoppingList(listItems)
    }

    BasketTheme {
        ListsLayout(
            listsFlow = flowOf(PagingData.from(lists)),
            isEmpty = false
        )
    }
}
