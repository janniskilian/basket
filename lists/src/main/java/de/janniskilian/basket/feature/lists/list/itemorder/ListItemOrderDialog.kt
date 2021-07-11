package de.janniskilian.basket.feature.lists.list.itemorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.ui.fragments.BaseBottomSheetDialogFragment
import de.janniskilian.basket.feature.lists.databinding.ListItemOrderDialogBinding

@AndroidEntryPoint
class ListItemOrderDialog : BaseBottomSheetDialogFragment<ListItemOrderDialogBinding>() {

    private val args by navArgs<ListItemOrderDialogArgs>()

    private val viewModel: ListItemOrderViewModel by viewModels()

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ListItemOrderDialogBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setShoppingListId(ShoppingListId(args.shoppingListId))

        viewModel.isGroupedByCategory.observe(viewLifecycleOwner) {
            binding.isGroupedByCategoryCheckBox.isChecked = it
        }

        viewModel.dismiss.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        binding.button.setOnClickListener {
            viewModel.submit(binding.isGroupedByCategoryCheckBox.isChecked)
        }
    }
}
