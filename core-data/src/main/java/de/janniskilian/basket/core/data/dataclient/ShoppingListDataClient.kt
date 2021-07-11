package de.janniskilian.basket.core.data.dataclient

import androidx.paging.PagingData
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListId
import kotlinx.coroutines.flow.Flow

interface ShoppingListDataClient {

    suspend fun create(name: String, color: Int, isGroupedByCategory: Boolean): ShoppingListId

    suspend fun create(shoppingList: ShoppingList): ShoppingListId

    suspend fun get(shoppingListId: ShoppingListId): ShoppingList?

    fun getAsFlow(shoppingListId: ShoppingListId): Flow<ShoppingList>

    fun getAll(pageSize: Int): Flow<PagingData<ShoppingList>>

    fun getCount(): Flow<Int>

    suspend fun update(
        shoppingListId: ShoppingListId,
        name: String,
        color: Int,
        isGroupedByCategory: Boolean
    )

    suspend fun delete(shoppingListId: ShoppingListId)
}
