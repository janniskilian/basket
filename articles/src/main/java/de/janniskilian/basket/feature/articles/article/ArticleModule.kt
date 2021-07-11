package de.janniskilian.basket.feature.articles.article

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import de.janniskilian.basket.core.data.dataclient.DataClient

@InstallIn(ActivityRetainedComponent::class)
@Module
class ArticleModule {

    @Provides
    fun provideArticleUseCases(dataClient: DataClient): ArticleUseCases =
        ArticleUseCases(dataClient)
}
