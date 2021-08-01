package de.janniskilian.basket.core.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.janniskilian.basket.core.ui.compose.du

@Composable
fun BottomDrawerColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(1.du),
        modifier = Modifier
            .padding(
                horizontal = 1.du,
                vertical = 2.du
            )
            .then(modifier)
    ) {
        content()
    }
}
