package de.janniskilian.basket.core.test

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import de.janniskilian.basket.core.data.dataclient.ArticleDataClientImpl
import de.janniskilian.basket.core.data.dataclient.CategoryDataClientImpl
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.data.dataclient.DataClientImpl
import de.janniskilian.basket.core.data.defaultdata.DefaultDataImporter
import de.janniskilian.basket.core.data.defaultdata.DefaultDataLoader
import de.janniskilian.basket.core.data.dataclient.ShoppingListDataClientImpl
import de.janniskilian.basket.core.data.dataclient.ShoppingListItemDataClientImpl
import de.janniskilian.basket.core.data.localdb.LocalDatabase
import kotlinx.coroutines.runBlocking
import java.util.*

fun createTestDataClient(): DataClient {
    val application = ApplicationProvider.getApplicationContext<Application>()

    val localDatabase = Room
        .inMemoryDatabaseBuilder(application, LocalDatabase::class.java)
        .allowMainThreadQueries()
        .build()

    val dataClient = DataClientImpl(
        ArticleDataClientImpl(localDatabase.articleDao()),
        CategoryDataClientImpl(localDatabase.categoryDao()),
        ShoppingListDataClientImpl(localDatabase.shoppingListDao()),
        ShoppingListItemDataClientImpl(localDatabase.shoppingListItemDao()),
        localDatabase
    )

    runBlocking {
        DefaultDataImporter(dataClient, DefaultDataLoader(application)).run(Locale.GERMAN)
    }

    return dataClient
}
