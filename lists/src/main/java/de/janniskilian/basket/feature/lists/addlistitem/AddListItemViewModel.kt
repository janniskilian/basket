package de.janniskilian.basket.feature.lists.addlistitem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.util.sortedByName
import de.janniskilian.basket.feature.lists.AddListItemNavigationDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddListItemViewModel @Inject constructor(
    private val getSuggestionsUseCase: GetSuggestionsUseCase,
    private val listItemSuggestionClickedUseCase: ListItemSuggestionClickedUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shoppingListId = MutableStateFlow(
        ShoppingListId(
            savedStateHandle.get<Long>(AddListItemNavigationDestination.LIST_ID_PARAM)!!
        )
    )

    private val _input = MutableStateFlow("")

    val input: StateFlow<String>
        get() = _input

    val items: Flow<PagingData<ShoppingListItemSuggestion>> =
        _input
            .flatMapLatest { inputValue ->
                shoppingListId.flatMapConcat {
                    getSuggestionsUseCase.run(it, inputValue, PAGE_SIZE)
                }
            }
            .cachedIn(viewModelScope)

    fun setShoppingListId(id: ShoppingListId) {
        shoppingListId.value = id
    }

    fun setInput(input: String) {
        _input.value = input
    }

    fun clearInput() {
        setInput("")
    }

    fun suggestionItemClicked(suggestion: ShoppingListItemSuggestion) {
        viewModelScope.launch {
            listItemSuggestionClickedUseCase.run(shoppingListId.value, suggestion)
        }
    }

    fun inputDoneButtonClicked() {
        //items.value?.firstOrNull()?.let(::suggestionItemClicked)
    }

    companion object {

        private const val PAGE_SIZE = 50
    }
}
