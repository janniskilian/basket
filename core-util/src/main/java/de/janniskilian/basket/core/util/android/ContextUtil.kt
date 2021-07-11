package de.janniskilian.basket.core.util.android

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.view.Window
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlin.properties.ReadOnlyProperty

private const val DEFAULT_PREFERENCES_DATA_STORE_NAME = "default"
val Context.defaultPreferencesDataStore: DataStore<Preferences>
        by preferencesDataStore(name = DEFAULT_PREFERENCES_DATA_STORE_NAME)

fun Context.findWindow(): Window? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context.window
        context = context.baseContext
    }
    return null
}
