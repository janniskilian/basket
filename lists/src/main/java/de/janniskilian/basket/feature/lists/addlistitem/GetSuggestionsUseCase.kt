package de.janniskilian.basket.feature.lists.addlistitem

import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import androidx.paging.map
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ShoppingListId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetSuggestionsUseCase(private val dataClient: DataClient) {

    private val parser = ListItemInputParser()

    fun run(
        shoppingListId: ShoppingListId,
        input: String
    ): Flow<PagingData<ShoppingListItemSuggestion>> {
        val parsedInput = parser.parse(input)
        val articles =
            dataClient.article.getSuggestionWhereNameLike(parsedInput.name, shoppingListId)
        val amount = parsedInput.quantity.orEmpty() +
                parsedInput.unit
                    ?.let { " $it" }
                    .orEmpty()

        val exactMatch = dataClient
            .article
            .getCountWhereNameExactly(parsedInput.name)
            .map { it == 1 }

        return exactMatch.flatMapLatest { isExactMatchAvailable ->
            articles.map { result ->
                val itemSuggestions = result.map {
                    ShoppingListItemSuggestion(
                        it.article,
                        it.isExistingListItem,
                        true,
                        amount
                    )
                }

                if (input.isBlank() || isExactMatchAvailable) {
                    itemSuggestions
                } else {
                    itemSuggestions.insertHeaderItem(
                        item = ShoppingListItemSuggestion(
                            Article(
                                ArticleId(0),
                                parsedInput.name,
                                null
                            ),
                            isExistingListItem = false,
                            isExistingArticle = false,
                            quantity = amount
                        )
                    )
                }
            }
        }
    }
}
