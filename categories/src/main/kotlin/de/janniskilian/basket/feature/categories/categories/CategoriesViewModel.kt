package de.janniskilian.basket.feature.categories.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.ui.navigation.DefaultSearchBarViewModel
import de.janniskilian.basket.core.ui.navigation.SearchBarViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    dataClient: DataClient
) : ViewModel(), SearchBarViewModel by DefaultSearchBarViewModel() {

    val categories: Flow<PagingData<Category>> =
        searchBarInput
            .flatMapLatest {
                dataClient
                    .category
                    .get(it, PAGE_SIZE)
            }
            .cachedIn(viewModelScope)

    companion object {

        private const val PAGE_SIZE = 50
    }
}
