package de.janniskilian.basket.feature.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import de.janniskilian.basket.R
import de.janniskilian.basket.core.ui.fragments.ViewBaseFragment
import de.janniskilian.basket.databinding.SettingsFragmentBinding

class SettingsFragment : ViewBaseFragment<SettingsFragmentBinding>() {

    override val titleTextRes get() = R.string.preferences_title

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        SettingsFragmentBinding.inflate(inflater, container, false)
}
