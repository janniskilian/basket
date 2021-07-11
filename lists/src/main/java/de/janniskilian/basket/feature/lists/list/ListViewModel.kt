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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataClient: DataClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shoppingListId = MutableStateFlow(
        ShoppingListId(savedStateHandle.get<Long>(ListNavigationDestination.LIST_ID_PARAM)!!)
    )

    private var _listItemsRemoved = MutableSharedFlow<List<ShoppingListItem>>()
    private var _allListItemsSetIsChecked = MutableSharedFlow<List<ShoppingListItem>>()

    val shoppingList = shoppingListId
        .flatMapLatest {
            dataClient
                .shoppingList
                .getAsFlow(it)
        }
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
        shoppingList.value?.let { list ->
            if (!list.isEmpty) {
                viewModelScope.launch {
                    dataClient.shoppingListItem.setAllCheckedForShoppingList(list.id, isChecked)
                    _allListItemsSetIsChecked.emit(
                        list.items.filter { it.isChecked != isChecked }
                    )
                }
            }
        }
    }

    fun removeListItem(listItem: ShoppingListItem) {
        viewModelScope.launch {
            dataClient.shoppingListItem.delete(listItem.shoppingListId, listItem.article.id)
            _listItemsRemoved.emit(listOf(listItem))
        }
    }

    fun removeAllListItems() {
        shoppingList.value?.let {
            if (!it.isEmpty) {
                viewModelScope.launch {
                    dataClient.shoppingListItem.deleteAllForShoppingList(it.id)
                    _listItemsRemoved.emit(it.items)
                }
            }
        }
    }

    fun removeAllCheckedListItems() {
        shoppingList.value?.let { list ->
            if (!list.isEmpty) {
                viewModelScope.launch {
                    dataClient.shoppingListItem.deleteAllCheckedForShoppingList(list.id)
                    val checkedListItems = list.items.filter { it.isChecked }
                    _listItemsRemoved.emit(checkedListItems)
                }
            }
        }
    }

    fun undoRemoveListItems() {
        /*listItemsRemoved.value?.let {
            viewModelScope.launch {
                dataClient.shoppingListItem.create(it)
            }
        }*/
    }

    fun undoSetAllListItemsIsChecked() {
        /*allListItemsSetToChecked.value?.let {
            viewModelScope.launch {
                dataClient.shoppingListItem.update(it)
            }
        }*/
    }
}
