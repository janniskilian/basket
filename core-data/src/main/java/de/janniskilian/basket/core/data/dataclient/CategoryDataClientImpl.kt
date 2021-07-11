package de.janniskilian.basket.core.data.dataclient

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import de.janniskilian.basket.core.data.localdb.dao.RoomCategoryDao
import de.janniskilian.basket.core.data.localdb.entity.RoomCategory
import de.janniskilian.basket.core.data.localdb.transformation.modelToRoom
import de.janniskilian.basket.core.data.localdb.transformation.roomToModel
import de.janniskilian.basket.core.type.domain.Category
import de.janniskilian.basket.core.type.domain.CategoryId
import de.janniskilian.basket.core.util.function.withIOContext
import de.janniskilian.basket.core.util.withoutSpecialChars
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryDataClientImpl @Inject constructor(
    private val dao: RoomCategoryDao
) : CategoryDataClient {

    override suspend fun create(name: String) = withIOContext {
        dao.insert(RoomCategory(name, name.withoutSpecialChars()))
    }

    override suspend fun create(categories: List<RoomCategory>) = withIOContext {
        dao
            .insert(categories)
            .map(::CategoryId)
    }

    override fun get(categoryId: CategoryId) =
        dao
            .select(categoryId.value)
            .map { roomToModel(it) }

    override suspend fun getSuspend(categoryId: CategoryId) = withIOContext {
        dao
            .selectSuspend(categoryId.value)
            ?.let(::roomToModel)
    }

    override fun get(name: String, pageSize: Int): Flow<PagingData<Category>> =
        Pager(PagingConfig(pageSize)) {
            dao.select("${name.withoutSpecialChars()}%")
        }
            .flow
            .map { results ->
                results.map { roomToModel(it) }
            }

    override suspend fun getCount() = dao.selectCount()

    override suspend fun update(category: Category) = withIOContext {
        dao.update(modelToRoom(category))
    }

    override suspend fun update(categoryId: CategoryId, name: String) = withIOContext {
        dao.update(categoryId.value, name, name.withoutSpecialChars())
    }

    override suspend fun delete(categoryId: CategoryId) = withIOContext {
        dao.delete(categoryId.value)
    }
}
