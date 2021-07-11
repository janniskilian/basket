package de.janniskilian.basket

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.test.espresso.Espresso.pressBack
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.feature.main.App
import org.junit.Test

class ListsComposeUiTest : BaseUiTest() {

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @Test
    fun createAndEditShoppingList() {
        composeTestRule.setContent {
            BasketTheme {
                App()
            }
        }

        skipOnboarding()

        composeTestRule.runOnIdle {

            val name = "Test list"
            val newName = "Edited test list"

            clickOnContentDescription(R.string.create_list_icon_desc)
            writeTo(R.string.list_name_hint, name)
            clickOn(R.string.create_list_button)

            pressBack()

            clickOn(name)
        }

        /*assertDisplayed(name)

        onView(allOf(withId(R.id.moreButton), hasSibling(withText(name)))).perform(click())
        clickOn(R.string.action_menu_edit_list)

        writeTo(R.id.nameEditText, newName)
        clickOn(R.string.save_list_button)

        assertDisplayed(newName)

        onView(allOf(withId(R.id.moreButton), hasSibling(withText(newName)))).perform(click())
        clickOn(R.string.action_menu_delete_list)

        assertNotExist(newName)

        clickOn(R.string.undo)

        assertDisplayed(newName)*/
    }
}
