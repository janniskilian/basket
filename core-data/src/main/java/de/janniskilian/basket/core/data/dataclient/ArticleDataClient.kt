package de.janniskilian.basket.core.data.dataclient

import androidx.paging.DataSource
import androidx.paging.PagingData
import androidx.paging.PagingSource
import de.janniskilian.basket.core.data.localdb.entity.RoomArticle
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ArticleSuggestion
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.core.type.domain.ShoppingListId
import kotlinx.coroutines.flow.Flow

interface ArticleDataClient {

    suspend fun create(name: String, category: Category?): Article?

    suspend fun create(articles: List<RoomArticle>)

    suspend fun get(articleId: ArticleId): Article?

    fun getSuggestionWhereNameLike(
        name: String,
        shoppingListId: ShoppingListId,
        pageSize: Int
    ): Flow<PagingData<ArticleSuggestion>>

    fun getWhereNameLike(name: String = "", pageSize: Int): Flow<PagingData<Article>>

    fun getCountWhereNameExactly(name: String): Flow<Int>

    suspend fun update(article: Article)

    suspend fun update(articleId: ArticleId, name: String, categoryId: CategoryId?)

    suspend fun delete(articleId: ArticleId)
}
