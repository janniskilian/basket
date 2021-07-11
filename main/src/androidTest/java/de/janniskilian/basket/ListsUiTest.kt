package de.janniskilian.basket

/*@RunWith(AndroidJUnit4::class)
@LargeTest
class ListsUiTest : BaseUiTest() {

    @Test
    fun createAndEditAndDeleteAndRestoreList() {
        val name = "Test list"
        val newName = "Edited test list"

        clickOn(R.string.fab_create_shopping_list)
        writeTo(R.id.nameEditText, name)
        clickOn(R.string.create_list_button)

        clickBack()

        assertDisplayed(name)

        onView(allOf(withId(R.id.moreButton), hasSibling(withText(name)))).perform(click())
        clickOn(R.string.action_menu_edit_list)

        writeTo(R.id.nameEditText, newName)
        clickOn(R.string.save_list_button)

        assertDisplayed(newName)

        onView(allOf(withId(R.id.moreButton), hasSibling(withText(newName)))).perform(click())
        clickOn(R.string.action_menu_delete_list)

        assertNotExist(newName)

        clickOn(R.string.undo)

        assertDisplayed(newName)
    }
}*/
