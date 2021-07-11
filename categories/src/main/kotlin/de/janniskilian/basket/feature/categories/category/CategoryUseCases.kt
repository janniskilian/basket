package de.janniskilian.basket.feature.categories.category

import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.CategoryId

class CategoryUseCases(private val dataClient: DataClient) {

    suspend fun createCategory(name: String) {
        dataClient.category.create(name)
    }

    suspend fun editCategory(id: CategoryId, name: String) {
        dataClient.category.update(id, name)
    }

    suspend fun deleteCategory(id: CategoryId) {
        dataClient.category.delete(id)
    }
}
