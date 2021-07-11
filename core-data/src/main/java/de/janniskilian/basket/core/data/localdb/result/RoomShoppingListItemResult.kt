package de.janniskilian.basket.core.data.localdb.result

import androidx.room.Embedded
import androidx.room.Relation
import de.janniskilian.basket.core.data.localdb.entity.RoomArticle
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingListItem

data class RoomShoppingListItemResult(
    @Embedded
    val shoppingListItem: RoomShoppingListItem,

    @Relation(parentColumn = "articleId", entityColumn = "id", entity = RoomArticle::class)
    val article: RoomArticleResult
)
