package de.janniskilian.basket.core.data.localdb.result

import androidx.room.Embedded
import androidx.room.Relation
import de.janniskilian.basket.core.data.localdb.entity.RoomArticle
import de.janniskilian.basket.core.data.localdb.entity.RoomCategory

class RoomArticleResult(
    @Embedded
    val article: RoomArticle,

    @Relation(parentColumn = "categoryId", entityColumn = "id", entity = RoomCategory::class)
    val category: RoomCategory?
)
