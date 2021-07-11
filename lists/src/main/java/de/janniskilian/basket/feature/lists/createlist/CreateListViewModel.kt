package de.janniskilian.basket.feature.lists.createlist

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.ui.compose.BasketColors
import de.janniskilian.basket.core.util.android.maybe
import de.janniskilian.basket.feature.lists.CreateListNavigationDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val useCases: CreateListFragmentUseCases,
    private val dataClient: DataClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val colors: List<Color> = BasketColors.listColors

    private val shoppingListId: ShoppingListId? =
        savedStateHandle
            .get<Long>(CreateListNavigationDestination.LIST_ID_PARAM)
            ?.maybe()
            ?.let(::ShoppingListId)

    private val _name = MutableStateFlow("")

    private val _selectedColor = MutableStateFlow(colors.first())

    private val _error = MutableStateFlow(false)

    private val _startList = MutableSharedFlow<ShoppingListId>()

    private val _dismiss = MutableSharedFlow<Unit>()

    private var isGroupedByCategory = true

    val name: StateFlow<String>
        get() = _name

    val selectedColor: StateFlow<Color>
        get() = _selectedColor

    val error: StateFlow<Boolean>
        get() = _error

    val startList: SharedFlow<ShoppingListId>
        get() = _startList

    val dismiss: SharedFlow<Unit>
        get() = _dismiss

    init {
        if (shoppingListId != null) {
            viewModelScope.launch {
                dataClient
                    .shoppingList
                    .get(shoppingListId)
                    ?.let {
                        setName(it.name)
                        setSelectedColor(Color(it.color))
                        isGroupedByCategory = it.isGroupedByCategory
                    }
            }
        }
    }

    fun setName(name: String) {
        _name.value = name
        _error.value = false
    }

    fun setSelectedColor(color: Color) {
        _selectedColor.value = color
    }

    fun submitButtonClicked() {
        val id = shoppingListId
        val name = name.value
        when {
            name.isNullOrBlank() -> _error.value = true

            id == null ->
                viewModelScope.launch {
                    val createdListId = useCases.createList(
                        name,
                        _selectedColor.value.toArgb(),
                        isGroupedByCategory
                    )

                    _startList.emit(createdListId)
                }

            else -> {
                viewModelScope.launch {
                    useCases.updateList(
                        id,
                        name,
                        _selectedColor.value.toArgb(),
                        isGroupedByCategory
                    )
                    _dismiss.emit(Unit)
                }
            }
        }
    }
}
