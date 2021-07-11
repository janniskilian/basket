package de.janniskilian.basket.feature.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.janniskilian.basket.core.data.defaultdata.DefaultDataImporter
import de.janniskilian.basket.core.util.KEY_DEFAULT_DATA_IMPORTED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val defaultDataImporter: DefaultDataImporter,
    private val preferencesDataStore: DataStore<Preferences>
) : ViewModel() {

    private val _isImporting = MutableStateFlow(false)

    private val _startLists = MutableSharedFlow<Unit>()

    val startList: SharedFlow<Unit>
        get() = _startLists

    val isImporting: StateFlow<Boolean>
        get() = _isImporting

    fun importDefaultData(selectedLocale: Locale) {
        _isImporting.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                defaultDataImporter.run(selectedLocale)
            }

            preferencesDataStore.edit {
                it[KEY_DEFAULT_DATA_IMPORTED] = true
            }

            _startLists.emit(Unit)
        }
    }
}
