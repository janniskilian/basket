package de.janniskilian.basket.feature.articles.articles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import de.janniskilian.basket.core.type.domain.Article
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.textColorSecondary
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.core.util.test.createTestArticle
import de.janniskilian.basket.core.util.test.createTestCategory
import de.janniskilian.basket.feature.articles.ArticleNavigationDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@ExperimentalMaterialApi
@Composable
fun ArticlesContent(viewModel: ArticlesViewModel = hiltViewModel()) {
    val navController = LocalNavController.current

    ArticlesLayout(
        articlesFlow = viewModel.articles,
        articleClickListener = {
            navController.navigate(
                ArticleNavigationDestination.createRoute(it.id)
            )
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun ArticlesLayout(
    articlesFlow: Flow<PagingData<Article>>,
    articleClickListener: (Article) -> Unit = {}
) {
    val articleItems = articlesFlow.collectAsLazyPagingItems()

    LazyColumn {
        itemsIndexed(articleItems) { i, article ->
            if (article != null) {
                ArticleItem(article, articleClickListener)

                if (i < articleItems.itemCount - 1) {
                    Divider()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ArticleItem(article: Article, articleClickListener: (article: Article) -> Unit) {
    ListItem(
        trailing = {
            val category = article.category
            if (category != null) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.textColorSecondary
                )
            }
        },
        modifier = Modifier.clickable { articleClickListener(article) }
    ) {
        Text(
            text = article.name,
            style = MaterialTheme.typography.body1
        )
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun ArticlesLayoutPreview() {
    val categories = (0 until NUM_PREVIEW_CATEGORIES).map { createTestCategory() }
    val articles = (0 until NUM_PREVIEW_ARTICLES).map { createTestArticle(categories.random()) }

    BasketTheme {
        ArticlesLayout(
            articlesFlow = flowOf(PagingData.from(articles))
        )
    }
}

private const val NUM_PREVIEW_CATEGORIES = 5
private const val NUM_PREVIEW_ARTICLES = 20
