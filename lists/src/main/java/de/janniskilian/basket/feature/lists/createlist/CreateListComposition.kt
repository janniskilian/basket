package de.janniskilian.basket.feature.lists.createlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janniskilian.basket.core.ui.compose.BasketColors
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.BasketButton
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.ui.compose.strokeShade
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.feature.lists.ListNavigationDestination
import de.janniskilian.basket.feature.lists.ListsNavigationDestination
import de.janniskilian.basket.feature.lists.R
import kotlinx.coroutines.flow.collect


private const val SELECTED_COLOR_ITEM_SCALE = 1.2f

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun CreateListContent(
    viewModel: CreateListViewModel = hiltViewModel()
) {
    val listName by viewModel.name.collectAsState()
    val colors = viewModel.colors
    val selectedColor by viewModel.selectedColor.collectAsState()

    val navController = LocalNavController.current

    LaunchedEffect(viewModel.startList) {
        viewModel.startList.collect {
            navController.navigate(ListNavigationDestination.createRoute(it)) {
                popUpTo(ListsNavigationDestination.routeScheme)
            }
        }
    }

    CreateListLayout(
        listName = listName,
        colors = colors,
        selectedColor = selectedColor,
        onChangeListName = viewModel::setName,
        onSelectColor = viewModel::setSelectedColor,
        onCreateListButtonClick = viewModel::submitButtonClicked
    )
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun CreateListLayout(
    listName: String?,
    colors: List<Color>,
    selectedColor: Color?,
    onChangeListName: (listName: String) -> Unit = {},
    onSelectColor: (color: Color) -> Unit = {},
    onCreateListButtonClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.du)
    ) {
        OutlinedTextField(
            value = listName.orEmpty(),
            onValueChange = { onChangeListName(it) },
            label = { Text(stringResource(R.string.list_name_hint)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onCreateListButtonClick() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.du)
        )

        Text(
            text = stringResource(R.string.create_list_color_info),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.du, top = 3.du, end = 2.du)
        )

        CreateListColorsRow(colors, selectedColor, onSelectColor)

        BasketButton(
            text = stringResource(R.string.create_list_button),
            onClick = onCreateListButtonClick,
            modifier = Modifier.padding(top = 3.du, bottom = 1.du)
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
private fun ColumnScope.CreateListColorsRow(
    colors: List<Color>,
    selectedColor: Color?,
    onColorSelected: (color: Color) -> Unit
) {
    BasketTheme(isDarkTheme = true) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 2.du),
            horizontalArrangement = Arrangement.spacedBy(2.du, Alignment.CenterHorizontally),
            modifier = Modifier.padding(top = 1.du)
        ) {
            items(colors, key = { it.toArgb() }) { color ->
                val isSelected = color == selectedColor
                val scale by animateFloatAsState(
                    if (isSelected) SELECTED_COLOR_ITEM_SCALE else 1f,
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )

                Surface(
                    shape = CircleShape,
                    color = color,
                    border = BorderStroke(2.dp, MaterialTheme.colors.strokeShade),
                    onClick = { onColorSelected(color) },
                    modifier = Modifier
                        .size(5.du)
                        .scale(scale)
                ) {
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(1.du)
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun CreateListLayoutPreview() {
    BasketTheme {
        CreateListLayout(
            listName = "Supermarket",
            colors = BasketColors.listColors,
            selectedColor = BasketColors.listColors[2]
        )
    }
}
