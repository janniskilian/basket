package de.janniskilian.basket.feature.categories.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.feature.categories.CategoryNavigationDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val useCases: CategoryUseCases,
    private val dataClient: DataClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId = savedStateHandle
        .get<Long>(CategoryNavigationDestination.CATEGORY_ID_PARAM)
        ?.let(::CategoryId)

    private val _name = MutableStateFlow("")

    private val _error = MutableStateFlow<Boolean>(false)

    private val _dismiss = MutableSharedFlow<Unit>()

    val name: StateFlow<String>
        get() = _name

    val error: StateFlow<Boolean>
        get() = _error

    val dismiss: SharedFlow<Unit>
        get() = _dismiss

    init {
        if (categoryId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                dataClient.category
                    .getSuspend(categoryId)
                    ?.let {
                        setName(it.name)
                    }
            }
        }
    }

    fun setName(name: String) {
        _name.value = name
        _error.value = false
    }

    fun deleteButtonClicked() {
        categoryId?.let {
            viewModelScope.launch {
                useCases.deleteCategory(it)
                _dismiss.emit(Unit)
            }
        }
    }

    fun submitButtonClicked() {
        val name = name.value
        if (name.isNullOrBlank()) {
            _error.value = true
        } else {
            viewModelScope.launch {
                val id = categoryId
                if (id == null) {
                    useCases.createCategory(name)
                } else {
                    useCases.editCategory(id, name)
                }

                _dismiss.emit(Unit)
            }
        }
    }
}
