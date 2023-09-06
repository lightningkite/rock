package com.lightningkite.mppexample

typealias RouteProps = Map<String, String>
typealias RouteParams = Map<String, String>
typealias RouteMap = MutableMap<String, RouteNode>
typealias ScreenCreator = (props: RouteProps, params: RouteParams) -> RockScreen

class Router(
    routes: List<Route>, private val fallback: RockScreen
) {
    var isNavigating = false

    private val routeMap: RouteNode = RouteNode(
        create = null, children = mutableMapOf(), dynamicParam = null
    )

    init {
        routes.forEach { setupRoute(it) }
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
                    create = null, children = mutableMapOf(), dynamicParam = null
                )
            }
            map.children[routeKey]!!.dynamicParam = paramName
            if (isLeaf) {
                map.children[routeKey]!!.create = route.create
            } else {
                map = map.children[routeKey]!!
            }
        }
    }

    private fun segmentPath(path: String) =
        if (path == "/") listOf("/") else path.split("/").filter { it.isNotEmpty() }.toList()

    fun findScreen(location: String): RockScreen {
        val pathParts = location.split("?")
        val path = pathParts[0]
        val query = pathParts.getOrNull(1).decodeURLParams()
        return findScreen(path, query)
    }

    private fun findScreen(location: String, params: Map<String, String>): RockScreen {
        val segments = segmentPath(location)
        val props = mutableMapOf<String, String>()
        var route = routeMap
        var failed = false

        segments.forEach { segment ->
            if (!failed) {
                if (route.children.containsKey(segment)) route = route.children[segment]!!
                else if (route.children.containsKey("*")) {
                    route = route.children["*"]!!
                    props[route.dynamicParam!!] = segment
                } else failed = true
            }
        }

        if (failed || route.create == null) return fallback

        return route.create!!(props, params)
    }
}

data class Route(
    val path: String,
    val create: ScreenCreator,
)

data class RouteNode(
    var create: ScreenCreator?, val children: RouteMap, var dynamicParam: String?
)


interface RockScreen {
    fun ViewContext.render()
    fun createPath(): String
}

interface RockApp {
    fun ViewContext.render()
}

class RedirectException(val screen: RockScreen) : Exception()
