package de.janniskilian.basket.feature.onboarding

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import de.janniskilian.basket.core.data.dataclient.DataClient
import de.janniskilian.basket.core.data.defaultdata.DefaultDataImporter
import de.janniskilian.basket.core.data.defaultdata.DefaultDataLoader

@InstallIn(ActivityRetainedComponent::class)
@Module
class OnboardingModule {

    @Provides
    fun provideDefaultDataImporter(
        @ApplicationContext context: Context,
        dataClient: DataClient
    ): DefaultDataImporter =
        DefaultDataImporter(
            dataClient,
            DefaultDataLoader(context)
        )
}
