package de.janniskilian.basket.feature.lists.createlist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import de.janniskilian.basket.core.data.dataclient.DataClient

@InstallIn(ActivityRetainedComponent::class)
@Module
class CreateListModule {

    @Provides
    fun provideCreateListUseCases(dataClient: DataClient): CreateListUseCases =
        CreateListUseCases(dataClient)
}
