package de.janniskilian.basket

/*class UiTestRule(isSkipOnboarding: Boolean = true) : TestRule {

    private val activityTestRule = ActivityRule(isSkipOnboarding)
    private val clearPreferencesRule = ClearPreferencesRule()
    private val clearDatabaseRule = ClearDatabaseRule()
    private val clearFilesRule = ClearFilesRule()

    override fun apply(base: Statement, description: Description): Statement =
        RuleChain
            .outerRule(clearPreferencesRule)
            .around(clearDatabaseRule)
            .around(clearFilesRule)
            .around(activityTestRule)
            .apply(base, description)
}*/
