package de.janniskilian.basket.core.ui.navigation

import kotlinx.coroutines.flow.StateFlow

interface SearchBarViewModel {

    val searchBarVisible: StateFlow<Boolean>

    val searchBarInput: StateFlow<String>

    fun setSearchBarVisible(isVisible: Boolean)

    fun setSearchBarInput(input: String)
}
