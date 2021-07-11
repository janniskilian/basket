package de.janniskilian.basket.core.feature.categories

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.janniskilian.basket.core.feature.R
import de.janniskilian.basket.core.feature.databinding.CategoryItemBinding
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.util.android.setContainerTransformTransitionName
import de.janniskilian.basket.core.util.android.view.layoutInflater
import de.janniskilian.basket.core.util.android.view.recyclerview.GenericDiffItemCallback

class CategoriesAdapter : ListAdapter<CategoriesAdapter.Item, CategoriesAdapter.ViewHolder>(
    GenericDiffItemCallback { oldItem, newItem ->
        oldItem?.category?.id == newItem?.category?.id
    }
) {

    var clickListener: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CategoryItemBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            root.setOnClickListener { clickListener?.invoke(holder.adapterPosition) }

            name.text = item?.category?.name ?: root.context.getString(R.string.category_default)
            checkmarkIcon.isVisible = item.isSelected

            item.category?.let {
                root.setContainerTransformTransitionName(it.id.value)
            }
        }
    }

    class ViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    class Item(val category: Category?, val isSelected: Boolean = false)
}
