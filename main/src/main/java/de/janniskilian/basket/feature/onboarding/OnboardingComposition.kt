package de.janniskilian.basket.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import de.janniskilian.basket.R
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.BasketButton
import de.janniskilian.basket.core.ui.compose.du
import de.janniskilian.basket.core.ui.compose.onSurfaceShade
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.feature.lists.ListsNavigationDestination
import de.janniskilian.basket.feature.main.OnboardingDestination
import kotlinx.coroutines.flow.collect
import java.util.*

@ExperimentalAnimationApi
@Composable
fun OnboardingContent(viewModel: OnboardingViewModel = hiltViewModel()) {
    val localeStrings = stringArrayResource(R.array.onboarding_presets_languages)
    val locales = remember {
        localeStrings.map { Locale.forLanguageTag(it) }
    }
    var selectedLocale by remember { mutableStateOf(locales.first()) }

    val isImporting by viewModel.isImporting.collectAsState(false)

    val navController = LocalNavController.current

    LaunchedEffect(viewModel.startList) {
        viewModel.startList.collect {
            navController.popBackStack()
        }
    }

    OnboardingLayout(
        locales = locales,
        selectedLocale = selectedLocale,
        isImporting = isImporting,
        onLocaleClick = { selectedLocale = it },
        onButtonClick = { viewModel.importDefaultData(selectedLocale) }
    )
}

@ExperimentalAnimationApi
@Composable
private fun OnboardingLayout(
    locales: List<Locale>,
    selectedLocale: Locale,
    isImporting: Boolean,
    onLocaleClick: (locale: Locale) -> Unit = {},
    onButtonClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.du)
    ) {
        Icon(
            imageVector = Icons.Outlined.Translate,
            contentDescription = stringResource(R.string.onboarding_language_image_desc),
            tint = MaterialTheme.colors.onSurfaceShade,
            modifier = Modifier.size(12.du)
        )

        Text(
            text = stringResource(R.string.onboarding_headline),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(start = 2.du, top = 3.du, end = 2.du)
        )

        Text(
            text = stringResource(R.string.onboarding_info),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(start = 2.du, top = 1.du, end = 2.du)
        )

        LocaleRadioGroup(
            locales = locales,
            selectedLocale = selectedLocale,
            isEnabled = !isImporting,
            onLocaleClick = onLocaleClick
        )

        Box(
            modifier = Modifier
                .padding(top = 3.du)
                .size(5.du)
        ) {
            this@Column.AnimatedVisibility(
                visible = isImporting,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        BasketButton(
            text = stringResource(R.string.onboarding_button),
            onClick = onButtonClick,
            modifier = Modifier.padding(top = 2.du)
        )
    }
}

@Composable
private fun LocaleRadioGroup(
    locales: List<Locale>,
    selectedLocale: Locale?,
    isEnabled: Boolean,
    onLocaleClick: (locale: Locale) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 3.du)
            .selectableGroup()
    ) {
        locales.forEach { locale ->
            val isSelected = locale == selectedLocale

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(7.du)
                    .selectable(
                        selected = isSelected,
                        onClick = { onLocaleClick(locale) },
                        role = Role.RadioButton,
                        enabled = isEnabled
                    )
                    .padding(horizontal = 2.du),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = null
                )
                Text(
                    text = locale.displayLanguage,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 2.du)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
private fun OnboardingLayoutPreview() {
    BasketTheme {
        OnboardingLayout(
            locales = listOf(Locale.ENGLISH, Locale.GERMAN),
            selectedLocale = Locale.GERMAN,
            isImporting = true
        )
    }
}
