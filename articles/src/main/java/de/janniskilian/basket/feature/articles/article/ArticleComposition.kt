package de.janniskilian.basket.feature.articles.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.du

@Composable
fun ArticleContent(viewModel: ArticleViewModel = hiltViewModel()) {
    val articleName by viewModel.name.collectAsState()
    val category by viewModel.category.collectAsState()

    ArticleLayout(articleName)
}

@Composable
private fun ArticleLayout(articleName: String?) {
    Column(
        modifier = Modifier.padding(2.du)
    ) {
        Text(
            text = articleName.orEmpty(),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleLayoutPreview() {
    BasketTheme {
        ArticleLayout(articleName = "Test article")
    }
}
