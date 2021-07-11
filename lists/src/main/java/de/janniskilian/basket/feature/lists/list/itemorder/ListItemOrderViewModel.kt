package de.janniskilian.basket.feature.lists.list.itemorder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListId
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListItemOrderViewModel @Inject constructor(
    private val useCases: ListItemOrderUseCases,
    private val dataClient: DataClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var shoppingList: ShoppingList? = null

    private val _isGroupedByCategory = MutableStateFlow(true)

    private val _dismiss = MutableSharedFlow<Unit>()

    val isGroupedByCategory: StateFlow<Boolean>
        get() = _isGroupedByCategory

    val dismiss: SharedFlow<Unit>
        get() = _dismiss

    init {
        val shoppingListId = ShoppingListId(savedStateHandle.get<Long>("listId")!!)

        viewModelScope.launch {
            dataClient
                .shoppingList
                .get(shoppingListId)
                ?.let {
                    shoppingList = it
                    setIsGroupedByCategory(it.isGroupedByCategory)
                }
        }
    }

    fun submit() {
        shoppingList?.let {
            viewModelScope.launch {
                useCases.updateList(it, isGroupedByCategory.value)
                _dismiss.emit(Unit)
            }
        }
    }

    fun setIsGroupedByCategory(isGroupedByCategory: Boolean) {
        _isGroupedByCategory.value = isGroupedByCategory
    }
}
