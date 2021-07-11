package de.janniskilian.basket.feature.lists.addlistitem

import androidx.paging.CombinedLoadStates
import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import de.janniskilian.basket.core.data.dataclient.ArticleDataClient
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ArticleSuggestion
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.core.type.domain.ShoppingListId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetSuggestionsUseCaseTest {

    private val apples = Article(
        ArticleId(2),
        "Apples",
        Category(
            CategoryId(1),
            "Produce"
        )
    )
    private val bananas = Article(
        ArticleId(1),
        "Bananas",
        Category(
            CategoryId(1),
            "Produce"
        )
    )
    private val clementines = Article(
        ArticleId(3),
        "Clementines",
        Category(
            CategoryId(1),
            "Produce"
        )
    )

    private val articleDataClientResult = listOf(
        ArticleSuggestion(apples, true),
        ArticleSuggestion(bananas, false),
        ArticleSuggestion(clementines, false)
    )

    private val shoppingListId = ShoppingListId(1)

    fun useCase(hasExactMatch: Boolean): GetSuggestionsUseCase {
        val articleDataClient: ArticleDataClient = mock {
            on { getSuggestionWhereNameLike(any(), ShoppingListId(any()), any()) } doReturn flowOf(
                PagingData.from(articleDataClientResult)
            )

            on { getCountWhereNameExactly(any()) } doReturn flowOf(if (hasExactMatch) 1 else 0)
        }

        val dataClient: DataClient = mock {
            on { article } doReturn articleDataClient
        }

        return GetSuggestionsUseCase(dataClient)
    }

    @Test
    fun ascendingOrderingByArticleName() = runBlocking {
        assertEquals(
            listOf(
                ShoppingListItemSuggestion(
                    apples,
                    isExistingListItem = true,
                    isExistingArticle = true
                ),
                ShoppingListItemSuggestion(
                    bananas,
                    isExistingListItem = false,
                    isExistingArticle = true
                ),
                ShoppingListItemSuggestion(
                    clementines,
                    isExistingListItem = false,
                    isExistingArticle = true
                )
            ),
            useCase(hasExactMatch = false)
                .run(shoppingListId, "", PAGE_SIZE)
                .single()
                .collectData()
        )
    }

    @Test
    fun createArticleSuggestion() = runBlocking {
        assertEquals(
            ShoppingListItemSuggestion(
                Article(
                    ArticleId(
                        0
                    ),
                    "apple",
                    null
                ),
                isExistingListItem = false,
                isExistingArticle = false
            ),
            useCase(hasExactMatch = false)
                .run(shoppingListId, "apple", PAGE_SIZE)
                .single()
                .collectData()
                .first()
        )

        assertEquals(
            ShoppingListItemSuggestion(
                apples,
                isExistingListItem = true,
                isExistingArticle = true
            ),
            useCase(hasExactMatch = true)
                .run(shoppingListId, apples.name, PAGE_SIZE)
                .single()
                .collectData()
                .first()
        )
    }

    @Test
    fun addQuantityToSuggestions() = runBlocking {
        val formatedQuantity = "2 kg"

        assertEquals(
            listOf(
                ShoppingListItemSuggestion(
                    Article(
                        ArticleId(
                            0
                        ), "a", null
                    ),
                    isExistingListItem = false,
                    isExistingArticle = false,
                    quantity = formatedQuantity
                ),
                ShoppingListItemSuggestion(
                    apples,
                    isExistingListItem = true,
                    isExistingArticle = true,
                    quantity = formatedQuantity
                ),
                ShoppingListItemSuggestion(
                    bananas,
                    isExistingListItem = false,
                    isExistingArticle = true,
                    quantity = formatedQuantity
                ),
                ShoppingListItemSuggestion(
                    clementines,
                    isExistingListItem = false,
                    isExistingArticle = true,
                    quantity = formatedQuantity
                )
            ),
            useCase(hasExactMatch = false)
                .run(shoppingListId, "a 2kg", PAGE_SIZE)
                .single()
                .collectData()
        )
    }

    companion object {

        private const val PAGE_SIZE = 50
    }
}

@ExperimentalCoroutinesApi
suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
    val differCallback = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {
            // No implementation necessary.
        }

        override fun onInserted(position: Int, count: Int) {
            // No implementation necessary.
        }

        override fun onRemoved(position: Int, count: Int) {
            // No implementation necessary.
        }
    }
    val items = mutableListOf<T>()
    val differ = object : PagingDataDiffer<T>(differCallback, TestCoroutineDispatcher()) {

        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            newCombinedLoadStates: CombinedLoadStates,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            for (idx in 0 until newList.size)
                items.add(newList.getFromStorage(idx))

            onListPresentable()

            return null
        }
    }
    differ.collectFrom(this)
    return items
}
