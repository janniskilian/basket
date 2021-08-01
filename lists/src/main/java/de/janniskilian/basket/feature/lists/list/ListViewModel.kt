package de.janniskilian.basket.feature.lists.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.type.domain.ShoppingListItem
import de.janniskilian.basket.feature.lists.ListNavigationDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataClient: DataClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shoppingListId = ShoppingListId(
        savedStateHandle.get<Long>(ListNavigationDestination.LIST_ID_PARAM)!!
    )

    private var removedListItems: List<ShoppingListItem>? = null
    private var _listItemsRemoved = MutableSharedFlow<List<ShoppingListItem>>()

    private var listItemsSetChecked: List<ShoppingListItem>? = null
    private var _allListItemsSetIsChecked = MutableSharedFlow<List<ShoppingListItem>>()

    val shoppingList = dataClient
        .shoppingList
        .getAsFlow(shoppingListId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val listItemsRemoved: SharedFlow<List<ShoppingListItem>>
        get() = _listItemsRemoved

    val allListItemsSetToChecked: SharedFlow<List<ShoppingListItem>>
        get() = _allListItemsSetIsChecked

    fun listItemClicked(shoppingListItem: ShoppingListItem) {
        viewModelScope.launch {
            dataClient.shoppingListItem.update(
                shoppingListItem.copy(isChecked = !shoppingListItem.isChecked)
            )
        }
    }

    fun setAllListItemsChecked(isChecked: Boolean) {
        viewModelScope.launch {
            shoppingList.firstOrNull { it != null }?.let { list ->
                Timber.i("setAllListItemsChecked=$list")
                if (!list.isEmpty) {
                    dataClient.shoppingListItem.setAllCheckedForShoppingList(list.id, isChecked)
                    val items = list.items.filter { it.isChecked != isChecked }
                    listItemsSetChecked = items
                    _allListItemsSetIsChecked.emit(items)
                }
            }
        }
    }

    fun removeListItem(listItem: ShoppingListItem) {
        viewModelScope.launch {
            dataClient.shoppingListItem.delete(listItem.shoppingListId, listItem.article.id)
            val item = listOf(listItem)
            removedListItems = item
            _listItemsRemoved.emit(item)
        }
    }

    fun removeAllListItems() {
        viewModelScope.launch {
            shoppingList.firstOrNull { it != null }?.let {
                if (!it.isEmpty) {
                    dataClient.shoppingListItem.deleteAllForShoppingList(it.id)
                    removedListItems = it.items
                    _listItemsRemoved.emit(it.items)
                }
            }
        }
    }

    fun removeAllCheckedListItems() {
        viewModelScope.launch {
            shoppingList.firstOrNull { it != null }?.let { list ->
                if (!list.isEmpty) {
                    dataClient.shoppingListItem.deleteAllCheckedForShoppingList(list.id)
                    val checkedListItems = list.items.filter { it.isChecked }
                    removedListItems = checkedListItems
                    _listItemsRemoved.emit(checkedListItems)
                }
            }
        }
    }

    fun undoRemoveListItems() {
        viewModelScope.launch {
            dataClient.shoppingListItem.create(removedListItems.orEmpty())
            removedListItems = null
        }
    }

    fun undoSetAllListItemsIsChecked() {
        viewModelScope.launch {
            dataClient.shoppingListItem.update(listItemsSetChecked.orEmpty())
            listItemsSetChecked = null
        }
    }
}
