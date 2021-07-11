package de.janniskilian.basket.feature.articles

import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import de.janniskilian.basket.core.type.domain.ArticleId
import de.janniskilian.basket.core.ui.navigation.NavigationDestination
import de.janniskilian.basket.feature.articles.article.ArticleContent
import de.janniskilian.basket.feature.articles.articles.ArticlesContent

object ArticlesNavigationDestination : NavigationDestination {

    override val routeScheme get() = "articles"

    @ExperimentalMaterialApi
    @Composable
    override fun Content() {
        ArticlesContent()
    }
}

object ArticleNavigationDestination : NavigationDestination {

    const val ARTICLE_ID_PARAM = "articleId"

    override val routeScheme get() = "article/{$ARTICLE_ID_PARAM}"

    override val argumentsList
        get() = listOf(navArgument(ARTICLE_ID_PARAM) { type = NavType.LongType })

    fun createRoute(articleId: ArticleId? = null) = "article/${articleId?.value}"

    @Composable
    override fun Content() {
        ArticleContent()
    }
}
