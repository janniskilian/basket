package de.janniskilian.basket.core.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import de.janniskilian.basket.core.ui.R
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.du

@ExperimentalMaterialApi
@Composable
fun BaseBasketBottomAppBar(
    isNavigationRoot: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    actionMenu: @Composable RowScope.() -> Unit = {},
    onHomeButtonClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    BottomAppBar(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        IconButton(onClick = onHomeButtonClick) {
            if (isNavigationRoot) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.navigation_button_desc),
                    tint = contentColor
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.navigation_button_desc),
                    tint = contentColor
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 3.du),
            content = content
        )

        CompositionLocalProvider(
            LocalContentAlpha provides 1f
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.wrapContentWidth(align = End)
            ) {
                actionMenu()
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BasketBottomAppBar(
    isNavigationRoot: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    actionMenu: @Composable RowScope.() -> Unit = {},
    onHomeButtonClick: () -> Unit = {}
) {
    BaseBasketBottomAppBar(
        isNavigationRoot = isNavigationRoot,
        backgroundColor = backgroundColor,
        contentColor = Color.White,
        actionMenu = actionMenu,
        onHomeButtonClick = onHomeButtonClick,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun BasketInputBottomAppBar(
    isNavigationRoot: Boolean,
    inputValue: String,
    onInputValueChange: (value: String) -> Unit,
    onInputDoneAction: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier,
    onInputFocusLost: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    actionMenu: @Composable RowScope.() -> Unit = {},
    onHomeButtonClick: () -> Unit = {},
    requestInitialFocus: Boolean = false
) {
    BaseBasketBottomAppBar(
        isNavigationRoot = isNavigationRoot,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        actionMenu = actionMenu,
        onHomeButtonClick = onHomeButtonClick,
        modifier = modifier
    ) {
        val focusRequester = remember { FocusRequester() }

        BasicTextField(
            value = inputValue,
            onValueChange = onInputValueChange,
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = onInputDoneAction),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .focusRequester(focusRequester)
        )

        DisposableEffect(Unit) {
            if (requestInitialFocus) {
                focusRequester.requestFocus()
            }

            onDispose {}
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun BasketBottomAppBarPreview() {
    BasketTheme {
        BasketBottomAppBar(
            isNavigationRoot = false,
            title = "Lorem ipsum"
        )
    }
}
