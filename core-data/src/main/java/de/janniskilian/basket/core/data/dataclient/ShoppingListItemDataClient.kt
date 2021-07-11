package de.janniskilian.basket.core.data.dataclient

import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.type.domain.ShoppingListItem
import de.janniskilian.basket.core.type.domain.ShoppingListItemId
import kotlinx.coroutines.flow.Flow

interface ShoppingListItemDataClient {

    suspend fun create(
        shoppingListId: ShoppingListId,
        article: Article,
        quantity: String = "",
        comment: String = "",
        isChecked: Boolean = false
    )

    suspend fun create(shoppingListItems: List<ShoppingListItem>)

    suspend fun get(shoppingListItemId: ShoppingListItemId): ShoppingListItem?

    fun getAsFlow(shoppingListItemId: ShoppingListItemId): Flow<ShoppingListItem>

    suspend fun update(shoppingListItem: ShoppingListItem)

    suspend fun update(shoppingListItems: List<ShoppingListItem>)

    suspend fun setAllCheckedForShoppingList(shoppingListId: ShoppingListId, isChecked: Boolean)

    suspend fun delete(shoppingListId: ShoppingListId, articleId: ArticleId)

    suspend fun deleteAllForShoppingList(shoppingListId: ShoppingListId)

    suspend fun deleteAllCheckedForShoppingList(shoppingListId: ShoppingListId)
}
