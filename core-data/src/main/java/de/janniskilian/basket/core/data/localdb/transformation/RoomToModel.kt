package de.janniskilian.basket.core.data.localdb.transformation

import de.janniskilian.basket.core.data.localdb.entity.RoomCategory
import de.janniskilian.basket.core.data.localdb.result.RoomArticleResult
import de.janniskilian.basket.core.data.localdb.result.RoomShoppingListItemResult
import de.janniskilian.basket.core.data.localdb.result.RoomShoppingListResult
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListId
import de.janniskilian.basket.core.type.domain.ShoppingListItem
import de.janniskilian.basket.core.type.domain.ShoppingListItemId

fun roomToModel(category: RoomCategory?): Category =
    if (category == null) {
        Category.None
    } else {
        Category(
            CategoryId(category.id),
            category.name
        )
    }

fun roomToModel(articleResult: RoomArticleResult): Article {
    val article = articleResult.article

    return Article(
        ArticleId(article.id),
        article.name,
        roomToModel(articleResult.category)
    )
}

fun roomToModel(result: RoomShoppingListResult): ShoppingList {
    val shoppingList = result.shoppingList

    return ShoppingList(
        ShoppingListId(shoppingList.id),
        shoppingList.name,
        shoppingList.color,
        shoppingList.isGroupedByCategory,
        result.shoppingListItems.orEmpty().map {
            val shoppingListItem = it.shoppingListItem

            ShoppingListItem(
                ShoppingListItemId(shoppingListItem.id),
                ShoppingListId(shoppingList.id),
                roomToModel(it.article),
                shoppingListItem.quantity,
                shoppingListItem.comment,
                shoppingListItem.isChecked
            )
        }
    )
}

fun roomToModel(result: RoomShoppingListItemResult): ShoppingListItem {
    val shoppingListItem = result.shoppingListItem

    return ShoppingListItem(
        ShoppingListItemId(shoppingListItem.id),
        ShoppingListId(shoppingListItem.shoppingListId),
        roomToModel(result.article),
        shoppingListItem.quantity,
        shoppingListItem.comment,
        shoppingListItem.isChecked
    )
}
