package de.janniskilian.basket.feature.lists.addlistitem

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.test.createTestDataClient
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListId
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class ListItemSuggestionClickedUseCaseTest {

    private lateinit var useCase: ListItemSuggestionClickedUseCase

    private var shoppingListId = ShoppingListId(0)

    private lateinit var dataClient: DataClient

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dataClient = createTestDataClient()

        runBlocking {
            shoppingListId = dataClient.shoppingList.create("Test", Color.RED, true)
        }

        useCase = ListItemSuggestionClickedUseCase(dataClient)
    }

    @After
    fun destroy() {
        dataClient.close()
    }

    @Test
    @UiThreadTest
    fun addListItemWithExistingArticle() = runBlocking {
        val article = createListItemWithExistingArticle(ArticleId(1))
        val shoppingList = getShoppingList()

        assertEquals(1, shoppingList.items.size)
        assertEquals(article, shoppingList.items.first().article)
        assertFalse(shoppingList.items.first().isChecked)
        assertEquals(shoppingListId, shoppingList.items.first().shoppingListId)
    }

    @Test
    @UiThreadTest
    fun addListItemWithNewArticle() = runBlocking {
        val article = createListItemWithNewArticle("New test article")
        val shoppingList = getShoppingList()

        assertEquals(1, shoppingList.items.size)
        assertEquals(article.name, shoppingList.items.first().article.name)
        assertFalse(shoppingList.items.first().isChecked)
        assertEquals(shoppingListId, shoppingList.items.first().shoppingListId)
    }

    @Test
    @UiThreadTest
    fun removeListItem() = runBlocking {
        val article1 = createListItemWithExistingArticle(
            ArticleId(
                1
            )
        )
        val article2 = createListItemWithExistingArticle(
            ArticleId(
                2
            )
        )
        val article3 = createListItemWithNewArticle("New test article")

        assertEquals(3, getShoppingList().items.size)

        useCase.run(
            shoppingListId,
            ShoppingListItemSuggestion(
                article1,
                isExistingListItem = true,
                isExistingArticle = true
            )
        )
        useCase.run(
            shoppingListId,
            ShoppingListItemSuggestion(
                article2,
                isExistingListItem = true,
                isExistingArticle = true
            )
        )
        useCase.run(
            shoppingListId,
            ShoppingListItemSuggestion(
                article3,
                isExistingListItem = true,
                isExistingArticle = true
            )
        )

        assert(getShoppingList().items.isEmpty())
    }

    private suspend fun getShoppingList(): ShoppingList =
        dataClient.shoppingList.get(shoppingListId)!!

    private suspend fun createListItemWithExistingArticle(articleId: ArticleId): Article {
        val article = dataClient.article.get(articleId)
        assertNotNull(article)
        useCase.run(
            shoppingListId,
            ShoppingListItemSuggestion(
                article,
                isExistingListItem = false,
                isExistingArticle = true
            )
        )
        return article
    }

    private suspend fun createListItemWithNewArticle(articleName: String): Article {
        val article = Article(
            ArticleId(0),
            articleName,
            null
        )
        useCase.run(
            shoppingListId,
            ShoppingListItemSuggestion(
                article,
                isExistingListItem = false,
                isExistingArticle = false
            )
        )
        return article
    }
}
