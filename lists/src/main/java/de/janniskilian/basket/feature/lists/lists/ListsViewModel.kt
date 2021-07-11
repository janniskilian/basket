package de.janniskilian.basket.feature.lists.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.feature.lists.ShortcutController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val dataClient: DataClient,
    private val shortcutController: ShortcutController
) : ViewModel() {

    private var deletedShoppingList: ShoppingList? = null

    private var _shoppingListDeleted = MutableSharedFlow<ShoppingList>()

    val shoppingLists = dataClient
        .shoppingList
        .getAll(PAGE_SIZE)
        .cachedIn(viewModelScope)

    val isEmpty = dataClient
        .shoppingList
        .getCount()
        .map { it == 0 }
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val shoppingListDeleted: SharedFlow<ShoppingList>
        get() = _shoppingListDeleted

    fun deleteList(shoppingListId: ShoppingListId) {
        viewModelScope.launch {
            dataClient.shoppingList.get(shoppingListId)?.let { shoppingList ->
                viewModelScope.launch { dataClient.shoppingList.delete(shoppingList.id) }
                shortcutController.removeShoppingListShortcut(shoppingList)

                deletedShoppingList = shoppingList
                _shoppingListDeleted.emit(shoppingList)
            }
        }
    }

    fun restoreShoppingList() {
        deletedShoppingList?.let {
            deletedShoppingList = null

            viewModelScope.launch {
                dataClient.shoppingList.create(it)
                dataClient.shoppingListItem.create(it.items)
            }
        }
    }

    companion object {

        private const val PAGE_SIZE = 10
    }
}

