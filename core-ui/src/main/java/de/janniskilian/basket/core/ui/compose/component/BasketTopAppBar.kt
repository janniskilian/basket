package de.janniskilian.basket.core.ui.compose.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janniskilian.basket.core.ui.R
import de.janniskilian.basket.core.ui.compose.BasketTheme

@Composable
fun DefaultTopAppBar(@StringRes titleStringRes: Int?) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Text(
            titleStringRes?.let { stringResource(id = it) } ?: "",
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultTopAppBarPreview() {
    BasketTheme {
        DefaultTopAppBar(R.string.list_item_title)
    }
}
