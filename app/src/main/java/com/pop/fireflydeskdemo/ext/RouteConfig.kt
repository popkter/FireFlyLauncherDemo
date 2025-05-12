package com.pop.fireflydeskdemo.ext

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

object RouteConfig {
    @Serializable
    const val Launcher = "Launcher"

    @Serializable
    const val Navigation = "Navigation"
}


fun NavController.routeTo(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    if (currentDestination?.route != route) {
        navigate(route, builder)
    }
}
