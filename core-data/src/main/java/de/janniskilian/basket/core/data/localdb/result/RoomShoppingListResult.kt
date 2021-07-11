package de.janniskilian.basket.core.data.localdb.result

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingList
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingListItem

data class RoomShoppingListResult(
    @Embedded
    val shoppingList: RoomShoppingList,

    @Relation(
        parentColumn = "id",
        entityColumn = "shoppingListId",
        entity = RoomShoppingListItem::class
    )
    val shoppingListItems: List<RoomShoppingListItemResult>?
)

