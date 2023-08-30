package com.lightningkite.mppexample

class Router (
    private val context: ViewContext,
    private val routes: List<Route>
) {
    fun render(location: String) {
        println("Location: $location")
        routes.forEach {
            if (it.path == location)
                it.render(context)
        }
    }
}

expect object RockNavigator {
    var currentPath: String
    fun navigate(path: String, pushState: Boolean = true)
    var router: Router?
}

data class Route(
    val path: String,
    val render: ViewContext.() -> Unit
)
