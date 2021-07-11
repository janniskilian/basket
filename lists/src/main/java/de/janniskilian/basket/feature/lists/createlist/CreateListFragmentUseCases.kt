package de.janniskilian.basket.feature.lists.createlist

import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingListId

class CreateListFragmentUseCases(private val dataClient: DataClient) {

    suspend fun createList(name: String, color: Int, isGroupedByCategory: Boolean): ShoppingListId =
        dataClient.shoppingList.create(name, color, isGroupedByCategory)

    suspend fun updateList(
        shoppingListId: ShoppingListId,
        name: String,
        color: Int,
        isGroupedByCategory: Boolean
    ) {
        dataClient.shoppingList.update(shoppingListId, name, color, isGroupedByCategory)
    }
}
