package de.janniskilian.basket.core.data.dataclient

import androidx.paging.PagingData
import de.janniskilian.basket.core.data.localdb.entity.RoomCategory
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.CategoryId
import kotlinx.coroutines.flow.Flow

interface CategoryDataClient {

    suspend fun create(name: String)

    suspend fun create(categories: List<RoomCategory>): List<CategoryId>

    fun get(categoryId: CategoryId): Flow<Category>

    suspend fun getSuspend(categoryId: CategoryId): Category?

    fun get(name: String = "", pageSize: Int): Flow<PagingData<Category>>

    suspend fun getCount(): Int

    suspend fun update(category: Category)

    suspend fun update(categoryId: CategoryId, name: String)

    suspend fun delete(categoryId: CategoryId)
}
