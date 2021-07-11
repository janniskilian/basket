package de.janniskilian.basket.feature.articles.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.feature.articles.ArticleNavigationDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val useCases: ArticleUseCases,
    private val dataClient: DataClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleId = savedStateHandle
        .get<Long>(ArticleNavigationDestination.ARTICLE_ID_PARAM)
        ?.let(::ArticleId)

    private val _name = MutableStateFlow<String>("")

    private val _category = MutableStateFlow<Category?>(null)

    private val _mode = MutableStateFlow(ArticleScreenMode.EDIT)

    private val _error = MutableStateFlow<Boolean>(false)

    private val _dismiss = MutableSharedFlow<Unit>()

    val name: StateFlow<String>
        get() = _name

    val category: StateFlow<Category?>
        get() = _category

    val categories = dataClient
        .category
        .get(pageSize = CATEGORY_PAGE_SIZE)
        .map {
            it.insertHeaderItem(item = Category.None)
        }
        .cachedIn(viewModelScope)

    val mode: StateFlow<ArticleScreenMode>
        get() = _mode

    val error: StateFlow<Boolean>
        get() = _error

    val dismiss: SharedFlow<Unit>
        get() = _dismiss

    init {
        if (articleId != null) {
            viewModelScope.launch {
                dataClient
                    .article
                    .get(articleId)
                    ?.let {
                        setName(it.name)
                        setCategory(it.category)
                    }
            }
        }
    }

    fun setName(name: String) {
        _name.value = name
        _error.value = false
    }

    fun setCategory(category: Category?) {
        _category.value = category
        _mode.value = ArticleScreenMode.EDIT
    }

    fun editCategoryClicked() {
        _mode.value = ArticleScreenMode.SELECT_CATEGORY
    }

    fun backPressed(): Boolean =
        if (_mode.value == ArticleScreenMode.EDIT) {
            false
        } else {
            _mode.value = ArticleScreenMode.EDIT
            true
        }

    fun deleteButtonClicked() {
        articleId?.let {
            viewModelScope.launch {
                useCases.deleteArticle(it)
                _dismiss.emit(Unit)
            }
        }
    }

    fun submitButtonClicked() {
        val name = name.value
        if (name.isBlank()) {
            _error.value = true
        } else {
            viewModelScope.launch {
                val id = articleId
                if (id == null) {
                    useCases.createArticle(name, category.value)
                } else {
                    useCases.editArticle(id, name, category.value)
                }

                _dismiss.emit(Unit)
            }
        }
    }

    companion object {

        private const val CATEGORY_PAGE_SIZE = 50
    }
}
