package de.janniskilian.basket.core.data.dataclient

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import de.janniskilian.basket.core.data.localdb.dao.RoomShoppingListDao
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingList
import de.janniskilian.basket.core.data.localdb.transformation.modelToRoom
import de.janniskilian.basket.core.data.localdb.transformation.roomToModel
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.util.function.withIOContext
import de.janniskilian.basket.core.util.withoutSpecialChars
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ShoppingListDataClientImpl @Inject constructor(
    private val dao: RoomShoppingListDao
) : ShoppingListDataClient {

    override suspend fun create(
        name: String,
        color: Int,
        isGroupedByCategory: Boolean
    ) = withIOContext {
        ShoppingListId(
            dao.insert(
                RoomShoppingList(
                    name,
                    name.withoutSpecialChars(),
                    color,
                    isGroupedByCategory
                )
            )
        )
    }

    override suspend fun create(shoppingList: ShoppingList) = withIOContext {
        ShoppingListId(dao.insert(modelToRoom(shoppingList)))
    }

    override suspend fun get(shoppingListId: ShoppingListId) = withIOContext {
        val result = dao.select(shoppingListId.value)
        if (result == null) {
            null
        } else {
            roomToModel(result)
        }
    }

    override fun getAsFlow(shoppingListId: ShoppingListId): Flow<ShoppingList> =
        dao
            .selectFlow(shoppingListId.value)
            .mapNotNull { roomToModel(it) }

    override fun getAll(pageSize: Int): Flow<PagingData<ShoppingList>> =
        Pager(PagingConfig(pageSize)) {
            dao.selectAll()
        }
            .flow
            .map { it.map(::roomToModel) }

    override fun getCount() = dao.selectCount()

    override suspend fun update(
        shoppingListId: ShoppingListId,
        name: String,
        color: Int,
        isGroupedByCategory: Boolean
    ) =
        withIOContext {
            dao.update(
                shoppingListId.value,
                name,
                name.withoutSpecialChars(),
                color,
                isGroupedByCategory
            )
        }

    override suspend fun delete(shoppingListId: ShoppingListId) = withIOContext {
        dao.delete(shoppingListId.value)
    }
}
