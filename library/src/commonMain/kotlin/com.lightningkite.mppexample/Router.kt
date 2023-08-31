package com.lightningkite.mppexample

typealias RouteProps = Map<String, String>
typealias RouteMap = MutableMap<String, RouteNode>
typealias ScreenCreator = (props: RouteProps) -> RockScreen
typealias RouteRenderer = ViewContext.(props: RouteProps) -> Unit

class Router(
    private val context: ViewContext,
    private val theme: Theme,
    routes: List<Route>,
    private val fallback: ViewContext.() -> Unit
) {
    private val routeMap: RouteNode = RouteNode(
        render = null,
        children = mutableMapOf(),
        dynamicParam = null
    )

    init {
        routes.forEach { setupRoute(it) }
        context.run {
            navigator = PlatformNavigator(
                router = this@Router,
                context = this,
            )
        }
    }

    private fun setupRoute(route: Route) {
        val path = segmentPath(route.path)
        var map = routeMap
        path.forEachIndexed { idx, key ->
            val isLeaf = idx == path.lastIndex
            val isDynamic = key.startsWith("{") && key.endsWith("}")
            val routeKey = if (isDynamic) "*" else key
            val paramName = if (isDynamic) key.substring(1, key.lastIndex) else null
            if (!map.children.containsKey(routeKey)) {
                map.children[routeKey] = RouteNode(
                    render = null,
                    children = mutableMapOf(),
                    dynamicParam = null
                )
            }
            map.children[routeKey]!!.dynamicParam = paramName
            if (isLeaf) {
                map.children[routeKey]!!.render = {
                    val screen = route.render(it)
                    screen.run {
                        withTheme(this@Router.theme) {
                            render()
                        }
                    }
                }
            } else {
                map = map.children[routeKey]!!
            }
        }
    }

    private fun segmentPath(path: String) =
        if (path == "/") listOf("/") else path.split("/").filter { it.isNotEmpty() }.toList()

    fun render(location: String) {
        val segments = segmentPath(location)
        val props = mutableMapOf<String, String>()
        var route = routeMap
        var failed = false
        segments.forEach { segment ->
            if (!failed) {
                if (route.children.containsKey(segment))
                    route = route.children[segment]!!
                else if (route.children.containsKey("*")) {
                    route = route.children["*"]!!
                    props[route.dynamicParam!!] = segment
                } else
                    failed = true
            }
        }

        context.run {
            box {
                id = "rock-screen-animate-in"
                if (!failed && route.render != null) {
                    route.render!!(props)
                } else {
                    fallback()
                }
            }
        }
    }
}

data class Route(
    val path: String,
    val render: ScreenCreator,
)

data class RouteNode(
    var render: RouteRenderer?,
    val children: RouteMap,
    var dynamicParam: String?
)


interface RockScreen {
    fun ViewContext.render()
    fun createPath(): String
}

interface RockApp {
    fun ViewContext.render()
}