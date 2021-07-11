package de.janniskilian.basket.feature.categories.category

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
fun CategoryContent(viewModel: CategoryViewModel = hiltViewModel()) {
    val categoryName by viewModel.name.collectAsState()

    CategoryLayout(categoryName)
}

@Composable
private fun CategoryLayout(categoryName: String?) {
    Column(
        modifier = Modifier.padding(2.du)
    ) {
        Text(
            text = categoryName.orEmpty(),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryLayoutPreview() {
    BasketTheme {
        CategoryLayout(categoryName = "Test category")
    }
}
