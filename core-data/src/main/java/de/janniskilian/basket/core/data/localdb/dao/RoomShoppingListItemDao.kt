package de.janniskilian.basket.core.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingListItem
import de.janniskilian.basket.core.data.localdb.result.RoomShoppingListItemResult
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomShoppingListItemDao {

    @Insert
    fun insert(shoppingListItem: RoomShoppingListItem)

    @Insert
    fun insert(shoppingListItems: List<RoomShoppingListItem>)

    @Transaction
    @Query("SELECT * FROM shoppingListItem WHERE id = :id")
    fun select(id: Long): RoomShoppingListItemResult?

    @Transaction
    @Query("SELECT * FROM shoppingListItem WHERE id = :id")
    fun selectFlow(id: Long): Flow<RoomShoppingListItemResult>

    @Update
    fun update(shoppingListItem: RoomShoppingListItem)

    @Update
    fun update(shoppingListItems: List<RoomShoppingListItem>)

    @Query("UPDATE shoppingListItem SET checked = :isChecked WHERE shoppingListId = :shoppingListId")
    fun setAllCheckedForShoppingList(
        shoppingListId: Long,
        isChecked: Boolean
    )

    @Query(
        """DELETE FROM shoppingListItem
			WHERE shoppingListId = :shoppingListId AND articleId = :articleId"""
    )
    fun delete(shoppingListId: Long, articleId: Long)

    @Query("DELETE FROM shoppingListItem WHERE shoppingListId = :shoppingListId")
    fun deleteAllForShoppingList(shoppingListId: Long)

    @Query("DELETE FROM shoppingListItem WHERE shoppingListId = :shoppingListId AND checked = 1")
    fun deleteAllCheckedForShoppingList(shoppingListId: Long)
}
