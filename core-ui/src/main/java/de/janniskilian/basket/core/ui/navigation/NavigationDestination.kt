package de.janniskilian.basket.core.ui.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NamedNavArgument

interface NavigationDestination {

    val routeScheme: String

    val argumentsList: List<NamedNavArgument> get() = emptyList()

    @Composable
    fun Content(arguments: Bundle?)

    fun isRouteFromDestination(route: String): Boolean =
        route.substringBeforeLast("/") == routeScheme.substringBeforeLast("/")
}
