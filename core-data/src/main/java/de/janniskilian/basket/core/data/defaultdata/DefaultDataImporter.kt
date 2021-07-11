package de.janniskilian.basket.core.data.defaultdata

import de.janniskilian.basket.core.data.dataclient.DataClient
import java.util.*

class DefaultDataImporter(
    private val dataClient: DataClient,
    private val defaultDataLoader: DefaultDataLoader
) {

    suspend fun run(locale: Locale) {
        if (dataClient.category.getCount() == 0) {
            createCategories(locale)
            createArticles(locale)
        }
    }

    private suspend fun createCategories(locale: Locale) {
        dataClient.category.create(defaultDataLoader.loadCategories(locale))
    }

    private suspend fun createArticles(locale: Locale) {
        dataClient.article.create(defaultDataLoader.loadArticles(locale))
    }
}
