package de.janniskilian.basket.core.data.localdb.transformation

import de.janniskilian.basket.core.data.localdb.entity.RoomCategory
import de.janniskilian.basket.core.data.localdb.result.RoomArticleResult
import de.janniskilian.basket.core.data.localdb.result.RoomArticleSuggestionResult
import de.janniskilian.basket.core.data.localdb.result.RoomShoppingListItemResult
import de.janniskilian.basket.core.data.localdb.result.RoomShoppingListResult
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.type.domain.ArticleSuggestion
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
            id = CategoryId(category.id),
            name = category.name
        )
    }

fun roomToModel(articleResult: RoomArticleResult): Article {
    val article = articleResult.article

    return Article(
        id = ArticleId(article.id),
        name = article.name,
        category = roomToModel(articleResult.category)
    )
}

fun roomToModel(result: RoomShoppingListResult): ShoppingList {
    val shoppingList = result.shoppingList

    return ShoppingList(
        id = ShoppingListId(shoppingList.id),
        name = shoppingList.name,
        color = shoppingList.color,
        isGroupedByCategory = shoppingList.isGroupedByCategory,
        items = result.shoppingListItems.orEmpty().map {
            val shoppingListItem = it.shoppingListItem

            ShoppingListItem(
                id = ShoppingListItemId(shoppingListItem.id),
                shoppingListId = ShoppingListId(shoppingList.id),
                article = roomToModel(it.article),
                quantity = shoppingListItem.quantity,
                comment = shoppingListItem.comment,
                isChecked = shoppingListItem.isChecked
            )
        }
    )
}

fun roomToModel(result: RoomShoppingListItemResult): ShoppingListItem {
    val shoppingListItem = result.shoppingListItem

    return ShoppingListItem(
        id = ShoppingListItemId(shoppingListItem.id),
        shoppingListId = ShoppingListId(shoppingListItem.shoppingListId),
        article = roomToModel(result.article),
        quantity = shoppingListItem.quantity,
        comment = shoppingListItem.comment,
        isChecked = shoppingListItem.isChecked
    )
}

fun roomToModel(
    result: RoomArticleSuggestionResult,
    shoppingListId: ShoppingListId
): ArticleSuggestion =
    ArticleSuggestion(
        article = Article(
            id = ArticleId(result.articleId),
            name = result.articleName,
            category = roomToModel(result.category)
        ),
        isExistingListItem = result.shoppingListId == shoppingListId.value
    )
