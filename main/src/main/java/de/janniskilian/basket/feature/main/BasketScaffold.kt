package de.janniskilian.basket.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import de.janniskilian.basket.R
import de.janniskilian.basket.core.ui.compose.BasketColors
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.BasketBottomAppBar
import de.janniskilian.basket.core.ui.compose.du


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun BasketScaffold(
    onHomeButtonClick: () -> Unit,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.systemBarsPadding(),
        content = content
    )
}

fun Modifier.contentImePadding(calculateBottomPadding: Dp): Modifier = composed {
    val ime = LocalWindowInsets.current.ime
    val navBars = LocalWindowInsets.current.navigationBars

    val density = LocalDensity.current

    with(density) {
        padding(
            //remember(density, ime, navBars) {
            PaddingValues(
                bottom = (ime.bottom - navBars.bottom - calculateBottomPadding.roundToPx())
                    .coerceAtLeast(0).toDp()
            )
            //}
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun AppScaffoldPreview() {
    BasketTheme {
        BasketScaffold(
            onHomeButtonClick = {},
            content = {}
        )
    }
}
