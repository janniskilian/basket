package de.janniskilian.basket.feature.lists.addlistitem

import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ShoppingListId

class ListItemSuggestionClickedUseCase(private val dataClient: DataClient) {

    suspend fun run(shoppingListId: ShoppingListId, suggestion: ShoppingListItemSuggestion) {
        when {
            suggestion.isExistingListItem ->
                dataClient.shoppingListItem.delete(shoppingListId, suggestion.article.id)

            suggestion.isExistingArticle ->
                createShoppingListItem(shoppingListId, suggestion.article, suggestion.quantity)

            else -> createArticleAndShoppingListItem(shoppingListId, suggestion)
        }
    }

    private suspend fun createShoppingListItem(
        shoppingListId: ShoppingListId,
        article: Article,
        quantity: String = ""
    ) {
        dataClient.shoppingListItem.create(
            shoppingListId,
            article,
            quantity
        )
    }

    private suspend fun createArticleAndShoppingListItem(
        shoppingListId: ShoppingListId,
        suggestion: ShoppingListItemSuggestion
    ) {
        dataClient.article
            .create(
                suggestion.article.name,
                suggestion.article.category
            )
            ?.let {
                createShoppingListItem(shoppingListId, it, suggestion.quantity)
            }
    }
}
