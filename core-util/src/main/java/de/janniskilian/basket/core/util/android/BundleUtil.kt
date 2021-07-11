package de.janniskilian.basket.core.util.android

import android.content.Intent
import android.os.Bundle

const val INT_NOTHING = -1
const val LONG_NOTHING = INT_NOTHING.toLong()

fun Bundle.getIntOrNull(key: String): Int? {
    val value = getInt(key, INT_NOTHING)
    return if (value == INT_NOTHING) {
        null
    } else {
        value
    }
}

fun Bundle.getLongOrNull(key: String): Long? {
    val value = getLong(key, LONG_NOTHING)
    return if (value == LONG_NOTHING) {
        null
    } else {
        value
    }
}

fun Intent.getLongExtraOrNull(key: String): Long? {
    val value = getLongExtra(key, LONG_NOTHING)
    return if (value == LONG_NOTHING) {
        null
    } else {
        value
    }
}

fun Long.maybe(): Long? =
    if (this == LONG_NOTHING) {
        null
    } else {
        this
    }
