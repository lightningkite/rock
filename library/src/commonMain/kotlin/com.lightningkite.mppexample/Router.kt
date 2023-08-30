package com.lightningkite.mppexample

typealias RouteProps = Map<String, String>
typealias RouteMap = MutableMap<String, RouteNode>
typealias RouteRenderer = ViewContext.(props: RouteProps) -> Unit

class Router(
    private val context: ViewContext,
    private val routes: List<Route>,
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
    }

    private fun setupRoute(route: Route) {
        val path = segmentPath(route.path)
        var map = routeMap
        println("PROCESSING ROUTE ${route.path}")
        path.forEachIndexed { idx, key ->
            val isLeaf = idx == path.lastIndex
            val isDynamic = key.startsWith("{") && key.endsWith("}")
            val routeKey = if (isDynamic) "*" else key
            val paramName = if (isDynamic) key.substring(1, key.lastIndex) else null
            println("segment $idx: $key (leaf: $isLeaf, isDynamic: $isDynamic, routeKey: $routeKey)")
            if (!map.children.containsKey(routeKey)) {
                println("Adding child")
                println("dynamic param: ${key.substring(1, key.lastIndex)}")
                map.children[routeKey] = RouteNode(
                    render = null,
                    children = mutableMapOf(),
                    dynamicParam = null
                )
            }
            map.children[routeKey]!!.dynamicParam = paramName
            if (isLeaf) {
                println("is leaf")
                map.children[routeKey]!!.render = route.render
            } else {
                println("is not leaf")
                map = map.children[routeKey]!!
            }
        }
    }

    private fun segmentPath(path: String) =
        if (path == "/") listOf("/") else path.split("/").filter { it.isNotEmpty() }.toList()

    fun render(location: String) {
        println("Location: $location")

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

        if (failed) {
            println("NOT FOUND, USING FALLBACK")
            fallback(context)
        } else if (route.render == null) {
            throw Error("Expecting render method on route")
        } else {
            println("MATCHED ROUTE")
            route.render!!(context, props)
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
    val render: RouteRenderer,
)

data class RouteNode(
    var render: RouteRenderer?,
    val children: RouteMap,
    var dynamicParam: String?
)
