package de.janniskilian.basket

/*@RunWith(AndroidJUnit4::class)
@LargeTest
class ArticlesUiTest : BaseUiTest() {

    @Test
    fun createAndEditAndDeleteArticle() {
        val name = "Test article"
        val editedName = "Edited test article"

        createArticle(name)
        editArticle(name, editedName)
        deleteArticle(editedName)

        findArticleItem(editedName)
        getArticleItemViewInteraction(editedName).check(doesNotExist())
    }

    private fun createArticle(name: String) {
        navigateToBottomNavigationDrawerDestination(R.string.navigation_articles)

        clickOn(R.string.fab_create_article)

        writeTo(R.id.nameEditText, name)
        closeKeyboard()
        clickOn(R.id.categoryEditText)

        clickListItem(R.id.recyclerView, 1)
        clickOn(R.string.create_article_button)
    }

    private fun editArticle(name: String, editedName: String) {
        findArticleItem(name)
        assertArticleIsDisplayed(name)

        getArticleItemViewInteraction(name).perform(click())

        writeTo(R.id.nameEditText, editedName)
        closeKeyboard()
        clickOn(R.id.categoryEditText)
        clickListItem(R.id.recyclerView, 0)
        clickOn(R.string.save_article_button)
    }

    private fun deleteArticle(name: String) {
        findArticleItem(name)
        assertArticleIsDisplayed(name)

        getArticleItemViewInteraction(name).perform(click())

        clickOn(R.string.delete_article_button)
    }

    private fun findArticleItem(name: String) {
        clickOn(R.id.action_search)
        writeTo(R.id.searchBarEditText, name)
    }

    private fun getArticleItemViewInteraction(name: String) =
        onView(allOf(withChild(withText(name)), isDescendantOfA(withId(R.id.recyclerView))))

    private fun assertArticleIsDisplayed(editedName: String) {
        getArticleItemViewInteraction(editedName).check(matches(isDisplayed()))
    }
}*/
