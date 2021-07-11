package de.janniskilian.basket.core.feature.listitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingListItemId
import de.janniskilian.basket.core.util.android.viewmodel.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListItemViewModel @Inject constructor(private val dataClient: DataClient) : ViewModel() {

    private val listItemId = MutableLiveData<ShoppingListItemId>()

    private val _name = MutableLiveData<String>()

    private val _quantity = MutableLiveData<String>()

    private val _comment = MutableLiveData<String>()

    private val _nameError = MutableLiveData<Boolean>()

    private val _dismiss = SingleLiveEvent<Unit>()

    private val shoppingListItem = listItemId.switchMap {
        dataClient
            .shoppingListItem
            .getAsFlow(it)
            .asLiveData()
    }

    val shoppingList = shoppingListItem.switchMap {
        dataClient
            .shoppingList
            .getAsFlow(it.shoppingListId)
            .asLiveData()
    }

    val name: LiveData<String>
        get() = _name

    val quantity: LiveData<String>
        get() = _quantity

    val comment: LiveData<String>
        get() = _comment

    val nameError: LiveData<Boolean>
        get() = _nameError

    val dismiss: LiveData<Unit>
        get() = _dismiss

    fun setListItemId(id: ShoppingListItemId) {
        listItemId.value = id

        viewModelScope.launch {
            dataClient.shoppingListItem
                .get(id)
                ?.let {
                    launch(Dispatchers.Main) {
                        setName(it.name)
                        setQuantity(it.quantity)
                        setComment(it.comment)
                    }
                }
        }
    }

    fun setName(name: String) {
        _name.value = name
        _nameError.value = false
    }

    fun setQuantity(quantity: String) {
        _quantity.value = quantity
    }

    fun setComment(comment: String) {
        _comment.value = comment
    }

    fun submit() {
        val articleName = name.value
        if (articleName.isNullOrBlank()) {
            _nameError.value = true
        } else {
            viewModelScope.launch {
                shoppingListItem.value?.let {
                    dataClient.article.update(
                        it.article.id,
                        articleName,
                        it.article.category?.id
                    )

                    dataClient.shoppingListItem.update(
                        it.copy(
                            quantity = quantity.value ?: it.quantity,
                            comment = comment.value ?: it.comment
                        )
                    )
                }

                _dismiss.postValue(Unit)
            }
        }
    }
}
