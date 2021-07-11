package de.janniskilian.basket.core.util.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No navigation controller found.")
}
