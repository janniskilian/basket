package de.janniskilian.basket.feature.lists.list.itemorder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.BasketButton
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.feature.lists.R
import kotlinx.coroutines.flow.collect

@Composable
fun ListItemOrderContent(viewModel: ListItemOrderViewModel = hiltViewModel()) {
    val navController = LocalNavController.current

    val isGroupedByCategory by viewModel.isGroupedByCategory.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.dismiss.collect {
            navController.popBackStack()
        }
    }

    ListItemOrderLayout(
        isGroupByCategory = isGroupedByCategory,
        onIsGroupedByCategoryChange = { viewModel.setIsGroupedByCategory(!isGroupedByCategory) },
        onApplyButtonClick = viewModel::submit
    )
}

@Composable
private fun ListItemOrderLayout(
    isGroupByCategory: Boolean,
    onIsGroupedByCategoryChange: () -> Unit = {},
    onApplyButtonClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(BottomCenter)
                .background(
                    color = MaterialTheme.colors.background,
                    shape = MaterialTheme.shapes.large.copy(
                        bottomStart = CornerSize(0),
                        bottomEnd = CornerSize(0)
                    )
                )
        ) {
            Text(
                text = stringResource(R.string.item_order_headline),
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.du, vertical = 3.du)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(7.du)
                    .clickable { onIsGroupedByCategoryChange() }
                    .padding(horizontal = 2.du),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isGroupByCategory,
                    onCheckedChange = null
                )
                Text(
                    text = stringResource(R.string.item_order_group_by_category),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 2.du)
                )
            }

            BasketButton(
                text = stringResource(R.string.apply),
                onClick = onApplyButtonClick,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(vertical = 3.du)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListItemOrderLayoutPreview() {
    BasketTheme {
        ListItemOrderLayout(isGroupByCategory = true)
    }
}
