package de.janniskilian.basket.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import de.janniskilian.basket.R
import de.janniskilian.basket.core.ui.navigation.NavigationDestination
import de.janniskilian.basket.feature.articles.ArticlesNavigationDestination
import de.janniskilian.basket.feature.categories.CategoriesNavigationDestination
import de.janniskilian.basket.feature.lists.ListsNavigationDestination

enum class NavigationRoot {

    LISTS {

        override val destination get() = ListsNavigationDestination

        override val nameResId get() = R.string.navigation_shopping_lists

        override val icon get() = Icons.Outlined.ListAlt
    },

    GROUPS {

        override val destination get() = GroupsNavigationDestination

        override val nameResId get() = R.string.navigation_groups

        override val icon get() = Icons.Outlined.GroupWork
    },

    ARTICLES {

        override val destination get() = ArticlesNavigationDestination

        override val nameResId get() = R.string.navigation_articles

        override val icon get() = Icons.Outlined.Sell
    },

    CATEGORIES {

        override val destination get() = CategoriesNavigationDestination

        override val nameResId get() = R.string.navigation_categories

        override val icon get() = Icons.Outlined.Category
    },

    SETTINGS {

        override val destination get() = SettingsNavigationDestination

        override val nameResId get() = R.string.navigation_settings

        override val icon get() = Icons.Outlined.Settings
    },

    HELP_AND_FEEDBACK {

        override val destination get() = HelpAndFeedbackNavigationDestination

        override val nameResId get() = R.string.navigation_help_and_feedback

        override val icon get() = Icons.Outlined.Help
    };

    abstract val destination: NavigationDestination

    abstract val nameResId: Int

    abstract val icon: ImageVector
}
