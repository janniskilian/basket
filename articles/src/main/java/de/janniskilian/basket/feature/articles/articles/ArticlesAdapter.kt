package de.janniskilian.basket.feature.articles.articles

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.util.android.setContainerTransformTransitionName
import de.janniskilian.basket.core.util.android.view.layoutInflater
import de.janniskilian.basket.core.util.android.view.recyclerview.GenericDiffItemCallback
import de.janniskilian.basket.feature.articles.R
import de.janniskilian.basket.feature.articles.databinding.ArticleItemBinding

class ArticlesAdapter : ListAdapter<Article, ArticlesAdapter.ViewHolder>(
    GenericDiffItemCallback { oldItem, newItem ->
        oldItem.id == newItem.id
    }
) {

    var clickListener: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ArticleItemBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            root.setOnClickListener {
                clickListener?.invoke(holder.adapterPosition)
            }

            articleName.text = item.name
            categoryName.text = item.category?.name
                ?: root.context.getString(R.string.category_default)

            root.setContainerTransformTransitionName(item.id.value)
        }
    }

    class ViewHolder(val binding: ArticleItemBinding) : RecyclerView.ViewHolder(binding.root)
}
