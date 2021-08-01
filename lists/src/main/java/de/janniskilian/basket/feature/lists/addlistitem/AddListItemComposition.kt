package de.janniskilian.basket.feature.lists.addlistitem

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import de.janniskilian.basket.core.ui.compose.BasketColors
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.BasketInputBottomAppBar
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.ui.compose.textColorSecondary
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.core.util.test.createTestArticle
import de.janniskilian.basket.feature.lists.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

@ExperimentalMaterialApi
@Composable
fun AddListItemContent(
    viewModel: AddListItemViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val input by viewModel.input.collectAsState()
    val itemsFlow = viewModel.items

    AddListItemLayout(
        inputValue = input,
        onInputValueChange = viewModel::setInput,
        onInputDoneAction = viewModel::inputDoneButtonClicked,
        onInputActionButtonClick = viewModel::clearInput,
        itemsFlow = itemsFlow,
        onHomeButtonClick = { navController.popBackStack() },
        onSuggestionClick = viewModel::suggestionItemClicked
    )
}

@ExperimentalMaterialApi
@Composable
private fun AddListItemLayout(
    inputValue: String,
    onInputValueChange: (value: String) -> Unit = {},
    onInputDoneAction: () -> Unit = {},
    onInputActionButtonClick: () -> Unit = {},
    itemsFlow: Flow<PagingData<ShoppingListItemSuggestion>>,
    onHomeButtonClick: () -> Unit = {},
    onSuggestionClick: (suggestion: ShoppingListItemSuggestion) -> Unit = {}
) {
    val items = itemsFlow.collectAsLazyPagingItems()

    Scaffold(
        bottomBar = {
            BasketInputBottomAppBar(
                isNavigationRoot = false,
                inputValue = inputValue,
                onInputValueChange = onInputValueChange,
                onInputDoneAction = { onInputDoneAction() },
                onInputFocusLost = onHomeButtonClick,
                requestInitialFocus = true,
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.textColorSecondary,
                actionMenu = {
                    IconButton(onClick = onInputActionButtonClick) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = null
                        )
                    }
                },
                onHomeButtonClick = onHomeButtonClick
            )
        }
    ) {
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> item.article.id.value }
            ) { i, item ->
                if (item != null) {
                    ListItemSuggestionItem(
                        item = item,
                        onSuggestionClick = onSuggestionClick
                    )

                    if (i < items.itemCount - 1) {
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ListItemSuggestionItem(
    item: ShoppingListItemSuggestion,
    onSuggestionClick: (suggestion: ShoppingListItemSuggestion) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(7.du)
            .clickable { onSuggestionClick(item) }
            .padding(horizontal = 2.du)
    ) {
        Text(
            text = formatItemSuggestion(item),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )

        val imageVector: ImageVector
        val tint: Color
        @StringRes val contentDescriptionRes: Int
        when {
            item.isExistingListItem -> {
                imageVector = Icons.Outlined.RemoveCircle
                tint = BasketColors.red
                contentDescriptionRes = R.string.existing_list_item_icon_desc
            }
            item.isExistingArticle -> {
                imageVector = Icons.Outlined.AddCircle
                tint = BasketColors.green
                contentDescriptionRes = R.string.existing_article_icon_desc
            }
            else -> {
                imageVector = Icons.Outlined.Create
                tint = MaterialTheme.colors.textColorSecondary
                contentDescriptionRes = R.string.create_new_article_icon_desc
            }
        }

        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescriptionRes),
            tint = tint
        )
    }
}

private fun formatItemSuggestion(suggestion: ShoppingListItemSuggestion): String =
    buildString {
        append(suggestion.article.name)

        if (suggestion.quantity.isNotBlank()) {
            append(", ${suggestion.quantity}")
        }
    }

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun AddListItemLayoutPreview() {
    val articles = (0 until NUM_PREVIEW_LIST_ITEMS).map { createTestArticle() }
    val suggestions = articles.mapIndexed { i, article ->
        ShoppingListItemSuggestion(article, i % PREVIEW_SELECTION_STEP != 0, true)
    }

    BasketTheme {
        AddListItemLayout(
            inputValue = "Test",
            itemsFlow = flowOf(PagingData.from(suggestions))
        )
    }
}

private const val NUM_PREVIEW_LIST_ITEMS = 20
private const val PREVIEW_SELECTION_STEP = 5
