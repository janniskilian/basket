package de.janniskilian.basket.core.data.localdb.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import de.janniskilian.basket.core.data.localdb.entity.RoomArticle
import de.janniskilian.basket.core.data.localdb.result.RoomArticleResult
import de.janniskilian.basket.core.data.localdb.result.RoomArticleSuggestionResult
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomArticleDao {

    @Insert
    fun insert(article: RoomArticle): Long

    @Insert
    fun insert(articles: List<RoomArticle>)

    @Transaction
    @Query("SELECT * FROM article WHERE id = :id")
    fun selectSuggestionWhereNameLike(id: Long): RoomArticleResult?

    @Query(
        """SELECT DISTINCT article.id AS articleId, article.name AS articleName,
			category.id AS category_id, category.name AS category_name,
            category.searchName AS category_searchName,
			listItem.shoppingListId AS shoppingListId
			FROM article
			LEFT OUTER JOIN category
			ON article.categoryId = category.id
			LEFT OUTER JOIN
			(SELECT articleId, shoppingListId FROM shoppingListItem
			WHERE shoppingListId = :shoppingListId) AS listItem
			ON article.id = listItem.articleId
			WHERE article.searchName LIKE :searchName
            ORDER BY article.searchName"""
    )
    fun selectSuggestionWhereNameLike(
        searchName: String,
        shoppingListId: Long
    ): PagingSource<Int, RoomArticleSuggestionResult>

    @Transaction
    @Query("SELECT * FROM article WHERE searchName LIKE :searchName ORDER BY searchName")
    fun selectWhereNameLike(searchName: String): PagingSource<Int, RoomArticleResult>

    @Query("SELECT COUNT(id) FROM article WHERE searchName = :searchName")
    fun selectCountWhereNameExactly(searchName: String): Flow<Int>

    @Update
    fun update(article: RoomArticle)

    @Query(
        """UPDATE article
            SET name = :name, searchName = :searchName, categoryId = :categoryId
            WHERE id = :id"""
    )
    fun update(id: Long, name: String, searchName: String, categoryId: Long?)

    @Query("DELETE FROM article WHERE id = :id")
    fun delete(id: Long)
}
