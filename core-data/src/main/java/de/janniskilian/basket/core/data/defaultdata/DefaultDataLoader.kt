package de.janniskilian.basket.core.data.defaultdata

import android.content.Context
import android.content.res.Configuration
import android.util.JsonReader
import android.util.JsonToken
import androidx.annotation.RawRes
import de.janniskilian.basket.core.data.R
import de.janniskilian.basket.core.data.localdb.entity.RoomArticle
import de.janniskilian.basket.core.data.localdb.entity.RoomCategory
import de.janniskilian.basket.core.util.function.withIOContext
import de.janniskilian.basket.core.util.withoutSpecialChars
import java.util.*

class DefaultDataLoader(private val context: Context) {

    private fun createLocalizedContext(locale: Locale): Context {
        val conf = Configuration(context.resources.configuration).also {
            it.setLocale(locale)
        }

        return context.createConfigurationContext(conf)
    }

    suspend fun loadCategories(locale: Locale): List<RoomCategory> = withIOContext {
        readArray(R.raw.categories, createLocalizedContext(locale)) { reader, _ ->
            var id: Long? = null
            var name: String? = null

            while (reader.hasNext()) {
                when (reader.nextName()) {
                    ID -> id = reader.nextLong()

                    NAME -> name = reader.nextString()

                    else -> reader.skipValue()
                }
            }

            if (id != null && name != null) {
                RoomCategory(name, name.withoutSpecialChars(), id)
            } else {
                null
            }
        }
    }

    suspend fun loadArticles(locale: Locale): List<RoomArticle> = withIOContext {
        readArray(R.raw.articles, createLocalizedContext(locale)) { reader, index ->
            var name: String? = null
            var categoryId: Long? = null

            while (reader.hasNext() && (name == null || categoryId == null)) {
                when (reader.nextName()) {
                    NAME -> name = reader.nextString()

                    CATEGORY_ID -> readArticleCategoryId(reader)?.let {
                        categoryId = it
                    }

                    else -> reader.skipValue()
                }
            }

            if (name != null) {
                RoomArticle(name, name.withoutSpecialChars(), categoryId, index + 1L)
            } else {
                null
            }
        }
    }

    private fun readArticleCategoryId(reader: JsonReader): Long? =
        if (reader.peek() == JsonToken.NUMBER) {
            reader.nextLong()
        } else {
            reader.skipValue()
            null
        }

    private inline fun <T> readArray(
        @RawRes resId: Int,
        localizedContext: Context,
        parse: (reader: JsonReader, index: Int) -> T?
    ): List<T> {
        val list = mutableListOf<T>()
        var index = 0

        JsonReader(
            localizedContext.resources
                .openRawResource(resId)
                .reader()
        ).use { reader ->
            reader.beginArray()

            while (reader.hasNext()) {
                reader.beginObject()
                parse(reader, index)?.let {
                    list.add(it)
                }
                reader.endObject()
                index++
            }

            reader.endArray()
        }

        return list
    }

    companion object {

        private const val ID = "id"
        private const val NAME = "name"
        private const val CATEGORY_ID = "categoryId"
    }
}
