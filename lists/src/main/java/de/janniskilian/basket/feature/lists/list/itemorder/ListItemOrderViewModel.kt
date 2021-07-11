package de.janniskilian.basket.feature.lists.list.itemorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.util.android.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListItemOrderViewModel @Inject constructor(
    private val useCases: ListItemOrderUseCases,
    private val dataClient: DataClient
) : ViewModel() {

    private var shoppingList: ShoppingList? = null

    private val _isGroupedByCategory = MutableLiveData<Boolean>()

    private val _dismiss = SingleLiveEvent<Unit>()

    val isGroupedByCategory: LiveData<Boolean>
        get() = _isGroupedByCategory

    val dismiss: LiveData<Unit>
        get() = _dismiss

    fun setShoppingListId(id: ShoppingListId) {
        viewModelScope.launch {
            dataClient
                .shoppingList
                .get(id)
                ?.let {
                    shoppingList = it
                    setIsGroupedByCategory(it.isGroupedByCategory)
                }
        }
    }

    fun submit(isGroupedByCategory: Boolean) {
        shoppingList?.let {
            viewModelScope.launch {
                useCases.updateList(it, isGroupedByCategory)
                _dismiss.postValue(Unit)
            }
        }
    }

    private fun setIsGroupedByCategory(isGroupedByCategory: Boolean) {
        _isGroupedByCategory.value = isGroupedByCategory
    }
}
