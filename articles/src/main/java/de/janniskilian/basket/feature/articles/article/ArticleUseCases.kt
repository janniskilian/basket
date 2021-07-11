package de.janniskilian.basket.feature.articles.article

import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.Category

class ArticleUseCases(private val dataClient: DataClient) {

    suspend fun createArticle(name: String, category: Category?) {
        dataClient.article.create(name, category)
    }

    suspend fun editArticle(articleId: ArticleId, name: String, category: Category?) {
        dataClient.article.update(articleId, name, category?.id)
    }

    suspend fun deleteArticle(articleId: ArticleId) {
        dataClient.article.delete(articleId)
    }
}
