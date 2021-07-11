package de.janniskilian.basket

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import de.janniskilian.basket.feature.main.MainActivity
import org.junit.Rule

open class BaseUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    fun skipOnboarding() {
        clickOn(R.string.onboarding_button)
    }

    fun getString(@StringRes resId: Int): String =
        composeTestRule.activity.getString(resId)

    fun clickOn(@StringRes resId: Int) {
        clickOn(getString(resId))
    }

    fun clickOn(text: String) {
        composeTestRule
            .onNodeWithText(text)
            .performClick()
    }

    fun clickOnContentDescription(@StringRes resId: Int) {
        composeTestRule
            .onNodeWithContentDescription(getString(resId))
            .performClick()
    }

    fun writeTo(@StringRes resId: Int, text: String) {
        composeTestRule
            .onNodeWithText(getString(resId))
            .performTextInput(text)
    }
}
