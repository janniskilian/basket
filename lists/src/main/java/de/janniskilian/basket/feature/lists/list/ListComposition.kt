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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.whenResumed
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListItem
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.BasketBottomAppBar
import de.janniskilian.basket.core.ui.compose.component.EmptyScreen
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.util.android.findWindow
import de.janniskilian.basket.core.util.android.keepScreenOn
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.core.util.sortedByName
import de.janniskilian.basket.core.util.test.createTestArticle
import de.janniskilian.basket.core.util.test.createTestCategory
import de.janniskilian.basket.core.util.test.createTestShoppingList
import de.janniskilian.basket.core.util.test.createTestShoppingListItem
import de.janniskilian.basket.feature.lists.AddListItemNavigationDestination
import de.janniskilian.basket.feature.lists.R
import de.janniskilian.basket.feature.lists.ResultCode
import de.janniskilian.basket.feature.lists.clearResult
import de.janniskilian.basket.feature.lists.getNavigationResult
import de.janniskilian.basket.feature.lists.getNavigationResultBundle
import de.janniskilian.basket.feature.lists.sendShoppingList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

private const val NO_CATEGORY_ID = -1L
private const val CHECKED_CATEGORY_ID = -2L

private const val KEY_PREFIX_CATEGORY = "CATEGORY_"
private const val KEY_PREFIX_LIST_ITEM = "LIST_ITEM_"


@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ListContent(
    viewModel: ListViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val shoppingList by viewModel.shoppingList.collectAsState()
    val listItemsRemoved by viewModel.listItemsRemoved.collectAsState(initial = null)

    KeepScreenOn()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        Timber.i("observe lifecycle")
        lifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_RESUME) {
                        val bundle = navController.getNavigationResultBundle()
                        Timber.i("onResume: $bundle")
                        bundle
                            ?.getNavigationResult { requestRoute, resultCode, data ->
                                Timber.i("getNavigationResult: $data")
                                navController.clearResult()

                                if (requestRoute.startsWith("list/moreMenu")
                                    && resultCode == ResultCode.SUCCESS
                                    && data is ListMoreMenuResult
                                ) {
                                    when (data.menuItem) {
                                        ListMoreMenuItem.CHECK_ALL -> viewModel.setAllListItemsChecked(
                                            true
                                        )
                                        ListMoreMenuItem.UNCHECK_ALL -> viewModel.setAllListItemsChecked(
                                            false
                                        )
                                        ListMoreMenuItem.REMOVE_CHECKED -> viewModel.removeAllCheckedListItems()
                                        ListMoreMenuItem.REMOVE_ALL -> viewModel.removeAllListItems()
                                        ListMoreMenuItem.SEND -> shoppingList?.let {
                                            sendShoppingList(
                                                context,
                                                it
                                            )
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        )

    }

    ListLayout(
        shoppingList = shoppingList,
        listItemsRemoved = listItemsRemoved,
        allListItemsSetToChecked = viewModel.allListItemsSetToChecked,
        onItemClick = viewModel::listItemClicked,
        onFabClick = {
            shoppingList?.id?.let {
                navController.navigate(AddListItemNavigationDestination.createRoute(it))
            }
        },
        onItemOrderButtonClick = {
            navController.navigate("list/itemOrder/${shoppingList!!.id.value}")
        },
        onHomeButtonClick = {
            navController.popBackStack()
        },
        onMoreButtonClick = {
            navController.navigate("list/moreMenu/${shoppingList!!.id.value}")
        },
        onUndoRemoveListItemsClick = {
            viewModel.undoRemoveListItems()
        },
        onUndoCheckListItemsClick = {
            viewModel.undoSetAllListItemsIsChecked()
        }
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun ListLayout(
    shoppingList: ShoppingList?,
    listItemsRemoved: List<ShoppingListItem>?,
    allListItemsSetToChecked: SharedFlow<List<ShoppingListItem>>,
    onItemClick: (item: ShoppingListItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    onItemOrderButtonClick: () -> Unit = {},
    onHomeButtonClick: () -> Unit = {},
    onMoreButtonClick: () -> Unit = {},
    onUndoRemoveListItemsClick: () -> Unit = {},
    onUndoCheckListItemsClick: () -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    ListItemsRemovedSnackbar(
        scaffoldState = scaffoldState,
        listItems = listItemsRemoved,
        onUndoClick = onUndoRemoveListItemsClick
    )

    AllListItemsSetToCheckedSnackbar(
        scaffoldState = scaffoldState,
        listItemsFlow = allListItemsSetToChecked,
        onUndoClick = onUndoCheckListItemsClick
    )

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.add_list_item_icon_desc)
                )
            }
        },
        bottomBar = {
            BasketBottomAppBar(
                isNavigationRoot = false,
                title = shoppingList?.name.orEmpty(),
                backgroundColor = shoppingList?.color?.let(::Color)
                    ?: MaterialTheme.colors.primarySurface,
                actionMenu = {
                    IconButton(onClick = onItemOrderButtonClick) {
                        Icon(
                            imageVector = Icons.Outlined.SwapVert,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = onMoreButtonClick) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = null
                        )
                    }
                },
                onHomeButtonClick = onHomeButtonClick
            )
        }
    ) {
        Crossfade(
            targetState = shoppingList?.isEmpty,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = it.calculateBottomPadding())
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
                    .sortedByName(),
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
            items = checkedListItems.sortedByName(),
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

@Composable
private fun ListItemsRemovedSnackbar(
    scaffoldState: ScaffoldState,
    listItems: List<ShoppingListItem>?,
    onUndoClick: () -> Unit
) {
    val undoText = stringResource(R.string.undo)

    if (!listItems.isNullOrEmpty()) {
        val placeholderText = if (listItems.size == 1) {
            listItems.first().name
        } else {
            listItems.size.toString()
        }

        val message = LocalContext.current.resources.getQuantityString(
            R.plurals.list_item_removed_snackbar,
            listItems.size,
            placeholderText
        )

        LaunchedEffect(System.currentTimeMillis()) {
            Timber.i("show snackbar: $message")
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message,
                undoText,
                SnackbarDuration.Long
            )

            if (result == SnackbarResult.ActionPerformed) {
                onUndoClick()
            }
        }
    }
}

@Composable
private fun AllListItemsSetToCheckedSnackbar(
    scaffoldState: ScaffoldState,
    listItemsFlow: SharedFlow<List<ShoppingListItem>>,
    onUndoClick: () -> Unit
) {
    val undoText = stringResource(R.string.undo)
    val uncheckedMessage = stringResource(R.string.all_list_items_unchecked_snackbar)
    val checkedMessage = stringResource(R.string.all_list_items_checked_snackbar)

    LaunchedEffect(Unit) {
        listItemsFlow.collect { listItems ->
            Timber.i("collect listItemsFlow")
            if (listItems.isNotEmpty()) {
                val message = if (listItems.first().isChecked) {
                    checkedMessage
                } else {
                    uncheckedMessage
                }

                Timber.i("show snackbar: $message")
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message,
                    undoText,
                    SnackbarDuration.Long
                )

                if (result == SnackbarResult.ActionPerformed) {
                    onUndoClick()
                }
            }
        }
    }
}

@Composable
private fun KeepScreenOn() {
    val view = LocalView.current
    val window = remember { view.context.findWindow() }
    LaunchedEffect(Unit) {
        window?.keepScreenOn(true)
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun ListLayoutPreview() {
    val categories = (0 until NUM_PREVIEW_CATEGORIES).map { createTestCategory() }
    val articles = (0 until NUM_PREVIEW_ARTICLES).map { createTestArticle(categories.random()) }

    var list = createTestShoppingList()
    val listItems = (0 until NUM_PREVIEW_LIST_ITEMS).map {
        createTestShoppingListItem(list, articles.random())
    }
    list = createTestShoppingList(listItems)

    BasketTheme {
        ListLayout(
            shoppingList = list,
            listItemsRemoved = list.items.subList(0, 3),
            allListItemsSetToChecked = MutableSharedFlow()
        )
    }
}

private const val NUM_PREVIEW_CATEGORIES = 5
private const val NUM_PREVIEW_ARTICLES = 20
private const val NUM_PREVIEW_LIST_ITEMS = 25
