package de.janniskilian.basket.feature.lists.addlistitem

import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import androidx.paging.map
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ArticleSuggestion
import de.janniskilian.basket.core.type.domain.ShoppingListId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import timber.log.Timber

class GetSuggestionsUseCase(private val dataClient: DataClient) {

    private val parser = ListItemInputParser()

    fun getSuggestions(
        shoppingListId: ShoppingListId,
        input: String,
        pageSize: Int
    ): Flow<PagingData<ShoppingListItemSuggestion>> {
        val parsedInput = parser.parse(input)
        val amount = createAmount(parsedInput.quantity, parsedInput.unit)
        val hasExactMatch = hasNameExactMatch(parsedInput.name)

        val articles = dataClient.article.getSuggestionsWhereNameLike(
            parsedInput.name,
            shoppingListId,
            pageSize
        )

        return hasExactMatch.flatMapLatest { isExactMatchAvailable ->
            articles.map { result ->
                val itemSuggestions = result.map {
                    createShoppingListItemSuggestion(it, amount)
                }

                if (input.isBlank() || isExactMatchAvailable) {
                    itemSuggestions
                } else {
                    itemSuggestions.insertHeaderItem(
                        item = createNewArticleSuggestion(parsedInput, amount)
                    )
                }
            }
        }
    }

    suspend fun getFirstSuggestion(
        shoppingListId: ShoppingListId,
        input: String
    ): ShoppingListItemSuggestion? {
        val parsedInput = parser.parse(input)
        val amount = createAmount(parsedInput.quantity, parsedInput.unit)
        val hasExactMatch = hasNameExactMatch(parsedInput.name).first()

        val article = dataClient.article.getFirstSuggestionWhereNameLike(
            parsedInput.name,
            shoppingListId
        )

        val itemSuggestion = article?.let {
            createShoppingListItemSuggestion(it, amount)
        }

        return if (input.isBlank() || hasExactMatch) {
            itemSuggestion
        } else {
            createNewArticleSuggestion(parsedInput, amount)
        }
    }

    private fun createAmount(quantity: String?, unit: String?): String =
        buildString {
            append(quantity.orEmpty())
            if (unit != null) {
                append(" ")
                append(unit)
            }
        }

    private fun hasNameExactMatch(name: String): Flow<Boolean> =
        dataClient
            .article
            .getCountWhereNameExactly(name)
            .map { it == 1 }

    private fun createShoppingListItemSuggestion(
        articleSuggestion: ArticleSuggestion,
        amount: String
    ): ShoppingListItemSuggestion =
        ShoppingListItemSuggestion(
            article = articleSuggestion.article,
            isExistingListItem = articleSuggestion.isExistingListItem,
            isExistingArticle = true,
            quantity = amount
        )

    private fun createNewArticleSuggestion(
        parsedInput: ListItemInputParser.Result,
        amount: String
    ): ShoppingListItemSuggestion =
        ShoppingListItemSuggestion(
            article = Article(
                id = ArticleId(0),
                name = parsedInput.name,
                category = null
            ),
            isExistingListItem = false,
            isExistingArticle = false,
            quantity = amount
        )
}
