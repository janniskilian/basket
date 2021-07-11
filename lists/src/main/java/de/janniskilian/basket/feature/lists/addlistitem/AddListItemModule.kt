package de.janniskilian.basket.feature.lists.addlistitem

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import de.janniskilian.basket.core.data.dataclient.DataClient

@InstallIn(ActivityRetainedComponent::class)
@Module
class AddListItemModule {

    @Provides
    fun provideGetSuggestionsUseCase(dataClient: DataClient): GetSuggestionsUseCase =
        GetSuggestionsUseCase(dataClient)

    @Provides
    fun provideCreateListItemUseCase(dataClient: DataClient): ListItemSuggestionClickedUseCase =
        ListItemSuggestionClickedUseCase(dataClient)
}
