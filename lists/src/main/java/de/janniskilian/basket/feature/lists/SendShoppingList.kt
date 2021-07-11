package de.janniskilian.basket.feature.lists

import android.content.Context
import android.content.Intent
import de.janniskilian.basket.core.type.domain.ShoppingList
import de.janniskilian.basket.core.util.MIME_TYPE_TEXT_PLAIN
import de.janniskilian.basket.core.util.NEWLINE

fun sendShoppingList(context: Context, shoppingList: ShoppingList) {
    val text = shoppingList
        .items
        .filter { !it.isChecked }
        .joinToString(separator = NEWLINE) {
            buildString {
                append(it.name)
                if (it.quantity.isNotBlank()) {
                    append(", ${it.quantity}")
                }
                if (it.comment.isNotBlank()) {
                    append(", ${it.comment}")
                }
            }
        }

    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = MIME_TYPE_TEXT_PLAIN
    }

    context.startActivity(Intent.createChooser(intent, null))
}


