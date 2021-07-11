package de.janniskilian.basket

import androidx.test.rule.ActivityTestRule
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

class ActivityRule(
    private val isSkipOnboarding: Boolean
) : ActivityTestRule<MainActivity>(MainActivity::class.java) {

    override fun afterActivityLaunched() {
        if (isSkipOnboarding) {
            clickOn(R.id.button)
        }
    }
}
