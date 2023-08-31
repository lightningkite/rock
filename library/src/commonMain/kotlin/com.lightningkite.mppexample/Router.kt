package com.lightningkite.mppexample

typealias RouteProps = Map<String, String>
typealias RouteMap = MutableMap<String, RouteNode>
typealias RouteRenderer = ViewContext.(props: RouteProps) -> Unit

class Router(
    private val context: ViewContext,
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
        println(routeMap)
        context.run {
            println("SETTING NAVIGATOR")
            navigator = RockNavigator(
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
                map.children[routeKey]!!.render = route.render
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

        if (failed || route.render == null) {
            fallback(context)
        } else {
            context.run {
                box {
                    id = "rock-screen-animate-in"
                    route.render!!(props)
                }
            }
        }
    }
}

interface IRockNavigator {
    var currentPath: String
    fun navigate(
        path: String,
        pushState: Boolean = true,
        transition: ScreenTransition = ScreenTransition.Push
    )
}

expect class RockNavigator(
    router: Router,
    context: ViewContext
) : IRockNavigator

class DummyRockNavigator : IRockNavigator {
    override var currentPath: String
        get() = throw NotImplementedError()
        set(value) = throw NotImplementedError()

    override fun navigate(path: String, pushState: Boolean, transition: ScreenTransition) {
        throw NotImplementedError()
    }
}

data class Route(
    val path: String,
    val render: RouteRenderer,
)

data class RouteNode(
    var render: RouteRenderer?,
    val children: RouteMap,
    var dynamicParam: String?
)
