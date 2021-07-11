package de.janniskilian.basket.core.data.dataclient

import de.janniskilian.basket.core.data.localdb.dao.RoomShoppingListItemDao
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingListItem
import de.janniskilian.basket.core.data.localdb.transformation.modelToRoom
import de.janniskilian.basket.core.data.localdb.transformation.roomToModel
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.type.domain.ShoppingListItem
import de.janniskilian.basket.core.type.domain.ShoppingListItemId
import de.janniskilian.basket.core.util.function.withIOContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShoppingListItemDataClientImpl @Inject constructor(
    private val dao: RoomShoppingListItemDao
) : ShoppingListItemDataClient {

    override suspend fun create(
        shoppingListId: ShoppingListId,
        article: Article,
        quantity: String,
        comment: String,
        isChecked: Boolean
    ) = withIOContext {
        dao.insert(
            RoomShoppingListItem(
                shoppingListId.value,
                article.id.value,
                quantity,
                comment,
                isChecked
            )
        )
    }

    override suspend fun create(shoppingListItems: List<ShoppingListItem>) = withIOContext {
        dao.insert(shoppingListItems.map { modelToRoom(it) })
    }

    override suspend fun get(shoppingListItemId: ShoppingListItemId) = withIOContext {
        dao
            .select(shoppingListItemId.value)
            ?.let(::roomToModel)
    }

    override fun getAsFlow(shoppingListItemId: ShoppingListItemId): Flow<ShoppingListItem> =
        dao
            .selectFlow(shoppingListItemId.value)
            .map { roomToModel(it) }

    override suspend fun update(shoppingListItem: ShoppingListItem) = withIOContext {
        dao.update(modelToRoom(shoppingListItem))
    }

    override suspend fun update(shoppingListItems: List<ShoppingListItem>) = withIOContext {
        dao.update(
            shoppingListItems.map { modelToRoom(it) }
        )
    }

    override suspend fun setAllCheckedForShoppingList(
        shoppingListId: ShoppingListId,
        isChecked: Boolean
    ) = withIOContext {
        dao.setAllCheckedForShoppingList(shoppingListId.value, isChecked)
    }

    override suspend fun delete(shoppingListId: ShoppingListId, articleId: ArticleId) =
        withIOContext {
            dao.delete(shoppingListId.value, articleId.value)
        }

    override suspend fun deleteAllForShoppingList(shoppingListId: ShoppingListId) = withIOContext {
        dao.deleteAllForShoppingList(shoppingListId.value)
    }

    override suspend fun deleteAllCheckedForShoppingList(shoppingListId: ShoppingListId) =
        withIOContext {
            dao.deleteAllCheckedForShoppingList(shoppingListId.value)
        }
}
