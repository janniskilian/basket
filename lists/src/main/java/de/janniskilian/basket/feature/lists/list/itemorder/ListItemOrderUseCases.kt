package de.janniskilian.basket.feature.lists.list.itemorder

import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.type.domain.ShoppingList

class ListItemOrderUseCases(private val dataClient: DataClient) {

    suspend fun updateList(shoppingList: ShoppingList, isGroupedByCategory: Boolean) {
        dataClient.shoppingList.update(
            shoppingList.id,
            shoppingList.name,
            shoppingList.color,
            isGroupedByCategory
        )
    }
}
