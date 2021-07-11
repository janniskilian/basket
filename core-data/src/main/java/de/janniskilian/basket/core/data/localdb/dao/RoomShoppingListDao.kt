package de.janniskilian.basket.core.data.localdb.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import de.janniskilian.basket.core.data.localdb.entity.RoomShoppingList
import de.janniskilian.basket.core.data.localdb.result.RoomShoppingListResult
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomShoppingListDao {

    @Insert
    fun insert(shoppingList: RoomShoppingList): Long

    @Transaction
    @Query("SELECT * FROM shoppingList WHERE shoppingList.id = :id")
    fun select(id: Long): RoomShoppingListResult?

    @Transaction
    @Query("SELECT * FROM shoppingList WHERE shoppingList.id = :id")
    fun selectFlow(id: Long): Flow<RoomShoppingListResult>

    @Transaction
    @Query("SELECT * FROM shoppingList ORDER BY searchName")
    fun selectAll(): PagingSource<Int, RoomShoppingListResult>

    @Query("SELECT COUNT(id) FROM shoppingList")
    fun selectCount(): Flow<Int>

    @Query(
        """UPDATE shoppingList 
			SET name = :name,
            searchName = :searchName,
            color = :color,
            isGroupedByCategory = :isGroupedByCategory
			WHERE id = :id"""
    )
    fun update(id: Long, name: String, searchName: String, color: Int, isGroupedByCategory: Boolean)

    @Query("DELETE FROM shoppingList WHERE id = :id")
    fun delete(id: Long)
}
