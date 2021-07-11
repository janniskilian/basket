package de.janniskilian.basket.core.util.android

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlin.properties.ReadOnlyProperty

val Context.hasHardwareKeyboard
    get() = resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS

private const val DEFAULT_PREFERENCES_DATA_STORE_NAME = "default"
val Context.defaultPreferencesDataStore: DataStore<Preferences>
        by preferencesDataStore(name = DEFAULT_PREFERENCES_DATA_STORE_NAME)
