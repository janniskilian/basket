package de.janniskilian.basket.core.type.domain

data class Category(
    val id: CategoryId,
    override val name: String
) : NamedItem {

    companion object {

        val None = Category(
            CategoryId(-1L),
            ""
        )
    }
}
