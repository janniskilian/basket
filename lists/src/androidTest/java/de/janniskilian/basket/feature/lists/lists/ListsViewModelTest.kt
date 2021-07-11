package de.janniskilian.basket.feature.lists.lists

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.test.createTestDataClient
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.util.android.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListsViewModelTest {

    private lateinit var viewModel: ListsViewModel

    private lateinit var dataClient: DataClient

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dataClient = createTestDataClient()
        viewModel = ListsViewModel(dataClient)
    }

    @After
    fun destroy() {
        dataClient.close()
    }

    @Test
    @UiThreadTest
    fun sortListsByName() = runBlocking {
        val id1 = dataClient.shoppingList.create("C", Color.RED, true)
        val id2 = dataClient.shoppingList.create("B", Color.RED, true)
        val id3 = dataClient.shoppingList.create("A", Color.RED, true)

        val shoppingLists = viewModel.shoppingLists.getOrAwaitValue()
        assert(shoppingLists.size == 3)
        assert(shoppingLists.component1().id == id3)
        assert(shoppingLists.component2().id == id2)
        assert(shoppingLists.component3().id == id1)
    }

    @Test
    @UiThreadTest
    fun deleteAndRestoreList(): Unit = runBlocking {
        val id1 = dataClient.shoppingList.create("Test", Color.RED, true)
        viewModel.shoppingLists
            .getOrAwaitValue()
            .let { lists ->
                assert(lists.indexOfFirst { it.id == id1 } == 1)
            }

        viewModel.deleteList(ShoppingListId(1))
        viewModel.shoppingLists
            .getOrAwaitValue()
            .let { lists ->
                assert(lists.isEmpty())
            }

        viewModel.restoreShoppingList()
        viewModel.shoppingLists
            .getOrAwaitValue()
            .let { lists ->
                assert(lists.indexOfFirst { it.id == id1 } == 1)
            }
    }
}
