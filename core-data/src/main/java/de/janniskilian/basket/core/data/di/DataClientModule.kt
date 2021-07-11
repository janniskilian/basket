package de.janniskilian.basket.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.janniskilian.basket.core.data.dataclient.ArticleDataClient
import de.janniskilian.basket.core.data.dataclient.ArticleDataClientImpl
import de.janniskilian.basket.core.data.dataclient.CategoryDataClient
import de.janniskilian.basket.core.data.dataclient.CategoryDataClientImpl
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.data.dataclient.DataClientImpl
import de.janniskilian.basket.core.data.dataclient.ShoppingListDataClient
import de.janniskilian.basket.core.data.dataclient.ShoppingListDataClientImpl
import de.janniskilian.basket.core.data.dataclient.ShoppingListItemDataClient
import de.janniskilian.basket.core.data.dataclient.ShoppingListItemDataClientImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataClientModule {

    @Singleton
    @Binds
    abstract fun provideDataClient(dataClientImpl: DataClientImpl): DataClient

    @Singleton
    @Binds
    abstract fun provideArticleDataClient(
        articleDataClientImpl: ArticleDataClientImpl
    ): ArticleDataClient

    @Singleton
    @Binds
    abstract fun provideCategoryDataClient(
        categoryDataClientImpl: CategoryDataClientImpl
    ): CategoryDataClient

    @Singleton
    @Binds
    abstract fun provideShoppingListDataClient(
        shoppingListDataClientImpl: ShoppingListDataClientImpl
    ): ShoppingListDataClient

    @Singleton
    @Binds
    abstract fun provideShoppingListItemDataClient(
        shoppingListItemDataClientImpl: ShoppingListItemDataClientImpl
    ): ShoppingListItemDataClient
}
