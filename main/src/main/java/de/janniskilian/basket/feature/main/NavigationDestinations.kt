package de.janniskilian.basket.feature.main

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import de.janniskilian.basket.core.ui.navigation.NavigationDestination
import de.janniskilian.basket.feature.groups.GroupsContent
import de.janniskilian.basket.feature.onboarding.OnboardingContent


object OnboardingDestination : NavigationDestination {

    override val routeScheme get() = "onboarding"

    @ExperimentalAnimationApi
    @Composable
    override fun Content() {
        OnboardingContent()
    }
}

object GroupsNavigationDestination : NavigationDestination {

    override val routeScheme get() = "groups"

    @ExperimentalMaterialApi
    @Composable
    override fun Content() {
        GroupsContent()
    }
}

object SettingsNavigationDestination : NavigationDestination {

    override val routeScheme get() = "settings"

    @Composable
    override fun Content() {
        // UI will be added later.
    }
}

object HelpAndFeedbackNavigationDestination : NavigationDestination {

    override val routeScheme get() = "helpAndFeedback"

    @Composable
    override fun Content() {
        // UI will be added later.
    }
}
