package de.janniskilian.basket.core.ui.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultSearchBarViewModel : SearchBarViewModel {

    private val _searchBarVisible = MutableStateFlow(false)

    private val _searchBarInput = MutableStateFlow("")

    override val searchBarVisible: StateFlow<Boolean>
        get() = _searchBarVisible

    override val searchBarInput: StateFlow<String>
        get() = _searchBarInput

    override fun setSearchBarVisible(isVisible: Boolean) {
        _searchBarVisible.value = isVisible
        if (!isVisible) {
            setSearchBarInput("")
        }
    }

    override fun setSearchBarInput(input: String) {
        _searchBarInput.value = input
    }
}
