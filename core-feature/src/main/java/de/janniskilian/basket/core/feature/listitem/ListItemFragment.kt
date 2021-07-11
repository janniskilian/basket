package de.janniskilian.basket.core.feature.listitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import dagger.hilt.android.AndroidEntryPoint
import de.janniskilian.basket.core.feature.R
import de.janniskilian.basket.core.ui.databinding.ListItemFragmentBinding
import de.janniskilian.basket.core.ui.fragments.ViewBaseFragment
import de.janniskilian.basket.core.util.function.createUiListColor

@AndroidEntryPoint
class ListItemFragment : ViewBaseFragment<ListItemFragmentBinding>() {

    private val args by lazy { ListItemFragmentArgs.fromBundle(requireArguments()) }

    private val viewModel: ListItemViewModel by viewModels()

    private val setup by lazy {
        ListItemFragmentSetup(this, args, viewModel)
    }

    override val useDefaultTransitions get() = false

    override val appBarColor by lazy {
        viewModel.shoppingList.map {
            createUiListColor(requireContext(), it.color)
        }
    }

    override val titleTextRes get() = R.string.list_item_title

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ListItemFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup.run()
    }
}
