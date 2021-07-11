package de.janniskilian.basket.feature.categories.category

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import de.janniskilian.basket.core.data.dataclient.DataClient

@InstallIn(ActivityRetainedComponent::class)
@Module
class CategoryModule {

    @Provides
    fun provideCategoryUseCases(dataClient: DataClient): CategoryUseCases =
        CategoryUseCases(dataClient)
}
