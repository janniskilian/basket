package de.janniskilian.basket.feature.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import dagger.hilt.android.AndroidEntryPoint
import de.janniskilian.basket.R
import de.janniskilian.basket.core.util.android.getBoolean
import de.janniskilian.basket.core.util.android.setDayNightMode
import javax.inject.Inject

@AndroidEntryPoint
class PreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private val systemDayNightModeSwitch
        get() = get<SwitchPreferenceCompat>(R.string.pref_key_system_day_night_mode)

    private val dayNightModeSwitch
        get() = get<SwitchPreferenceCompat>(R.string.pref_key_day_night_mode)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        updateDayNightModePreference()

        //setPreferencesFromResource(R.xml.preference_screen, rootKey)
        setupSystemDayNightModeSwitch()
        setupDayNightModeSwitch()
    }

    private fun setupSystemDayNightModeSwitch() {
        systemDayNightModeSwitch?.apply {
            onChange<Boolean> {
                val dayNightMode = dayNightModeSwitch?.isChecked
                    ?: getBoolean(context, R.bool.pref_def_day_night_mode)

                setDayNightMode(it, dayNightMode)

                dayNightModeSwitch?.isEnabled = !it
            }
        }
    }

    private fun setupDayNightModeSwitch() {
        dayNightModeSwitch?.apply {
            isEnabled = !sharedPrefs.getBoolean(
                getString(R.string.pref_key_system_day_night_mode),
                resources.getBoolean(R.bool.pref_def_system_day_night_mode)
            )

            onChange<Boolean> {
                if (isEnabled) {
                    val systemDayNightMode = systemDayNightModeSwitch?.isChecked
                        ?: getBoolean(context, R.bool.pref_def_system_day_night_mode)

                    setDayNightMode(systemDayNightMode, it)
                }
            }
        }
    }

    private fun updateDayNightModePreference() {
        val autoDayNightMode = sharedPrefs.getBoolean(
            getString(R.string.pref_key_system_day_night_mode),
            resources.getBoolean(R.bool.pref_def_system_day_night_mode)
        )
        if (autoDayNightMode) {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            sharedPrefs.edit {
                putBoolean(
                    getString(R.string.pref_key_day_night_mode),
                    currentNightMode == Configuration.UI_MODE_NIGHT_YES
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Preference> get(@StringRes resId: Int): T? =
        findPreference(getString(resId))

    private fun <T : Any> Preference.onChange(listener: (newValue: T) -> Unit) {
        setOnPreferenceChangeListener { _, newValue ->
            @Suppress("UNCHECKED_CAST")
            listener(newValue as T)
            true
        }
    }
}
