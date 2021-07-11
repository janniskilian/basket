package de.janniskilian.basket.feature.categories.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    dataClient: DataClient
) : ViewModel() {

    private val _searchBarVisible = MutableStateFlow(false)

    private val _searchBarInput = MutableStateFlow("")

    val searchBarVisible: StateFlow<Boolean>
        get() = _searchBarVisible

    val searchBarInput: StateFlow<String>
        get() = _searchBarInput

    val categories: Flow<PagingData<Category>> =
        searchBarInput
            .flatMapLatest {
                dataClient
                    .category
                    .get(it, PAGE_SIZE)
            }
            .cachedIn(viewModelScope)

    fun setSearchBarVisible(isVisible: Boolean) {
        _searchBarVisible.value = isVisible
        if (!isVisible) {
            setSearchBarInput("")
        }
    }

    fun setSearchBarInput(input: String) {
        _searchBarInput.value = input
    }

    companion object {

        private const val PAGE_SIZE = 50
    }
}
