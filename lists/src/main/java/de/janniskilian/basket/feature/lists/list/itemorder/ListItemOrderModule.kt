package de.janniskilian.basket.feature.lists.list.itemorder

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import de.janniskilian.basket.core.data.dataclient.DataClient

@InstallIn(ActivityRetainedComponent::class)
@Module
class ListItemOrderModule {

    @Provides
    fun provideArticleUseCases(dataClient: DataClient): ListItemOrderUseCases =
        ListItemOrderUseCases(dataClient)
}
