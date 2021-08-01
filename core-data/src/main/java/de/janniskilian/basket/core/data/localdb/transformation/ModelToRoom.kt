package de.janniskilian.basket.core.data.localdb.transformation

import de.janniskilian.basket.core.data.localdb.entity.RoomArticle
import de.janniskilian.basket.core.data.localdb.entity.RoomCategory
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingList
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingListItem
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.type.domain.ShoppingListItem
import de.janniskilian.basket.core.util.withoutSpecialChars

fun modelToRoom(category: Category): RoomCategory =
    RoomCategory(
        name = category.name,
        searchName = category.name.withoutSpecialChars(),
        id = category.id.value
    )

fun modelToRoom(article: Article): RoomArticle =
    RoomArticle(
        name = article.name,
        searchName = article.name.withoutSpecialChars(),
        categoryId = article.category?.id?.value,
        id = article.id.value
    )

fun modelToRoom(shoppingList: ShoppingList): RoomShoppingList =
    RoomShoppingList(
        name = shoppingList.name,
        searchName = shoppingList.name.withoutSpecialChars(),
        color = shoppingList.color,
        isGroupedByCategory = shoppingList.isGroupedByCategory,
        id = shoppingList.id.value
    )

fun modelToRoom(shoppingListItem: ShoppingListItem): RoomShoppingListItem =
    RoomShoppingListItem(
        shoppingListId = shoppingListItem.shoppingListId.value,
        articleId = shoppingListItem.article.id.value,
        quantity = shoppingListItem.quantity,
        comment = shoppingListItem.comment,
        isChecked = shoppingListItem.isChecked,
        id = shoppingListItem.id.value
    )
