package de.janniskilian.basket.core.feature.listitem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingListItemId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListItemViewModel @Inject constructor(
    private val dataClient: DataClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listItemId = MutableStateFlow(
        ShoppingListItemId(savedStateHandle.get<Long>("listItemId")!!)
    )

    private val _name = MutableStateFlow("")

    private val _quantity = MutableStateFlow("")

    private val _comment = MutableStateFlow("")

    private val _nameError = MutableStateFlow(false)

    private val _dismiss = MutableSharedFlow<Unit>()

    @ExperimentalCoroutinesApi
    private val shoppingListItem = listItemId
        .flatMapLatest {
            dataClient
                .shoppingListItem
                .getAsFlow(it)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    @ExperimentalCoroutinesApi
    val shoppingList = shoppingListItem
        .filterNotNull()
        .flatMapLatest {
            dataClient
                .shoppingList
                .getAsFlow(it.shoppingListId)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val name: StateFlow<String>
        get() = _name

    val quantity: StateFlow<String>
        get() = _quantity

    val comment: StateFlow<String>
        get() = _comment

    val nameError: StateFlow<Boolean>
        get() = _nameError

    val dismiss: SharedFlow<Unit>
        get() = _dismiss

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
        if (articleName.isBlank()) {
            _nameError.value = true
        } else {
            viewModelScope.launch {
                shoppingListItem
                    .first { it != null }
                    ?.let {
                        dataClient.article.update(
                            it.article.id,
                            articleName,
                            it.article.category?.id
                        )

                        dataClient.shoppingListItem.update(
                            it.copy(
                                quantity = quantity.value,
                                comment = comment.value
                            )
                        )

                        _dismiss.emit(Unit)
                    }
            }
        }
    }
}
