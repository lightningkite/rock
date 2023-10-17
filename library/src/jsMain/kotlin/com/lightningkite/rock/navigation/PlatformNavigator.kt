package com.lightningkite.rock.navigation

import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.SharedReadable
import com.lightningkite.rock.reactive.Writable
import kotlinx.browser.window
import org.w3c.dom.MANUAL
import org.w3c.dom.PopStateEvent
import org.w3c.dom.ScrollRestoration
import org.w3c.dom.url.URLSearchParams

actual class PlatformNavigator actual constructor(
    val routes: Routes
) : RockNavigator {
    private var nextIndex: Int = 1
    private var currentIndex: Int = 0

    private val params: Map<String, String>
        get() = window.location.search.split('&').associate { it.substringBefore('=') to it.substringAfter('=') }

    init {
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            val index = event.state as Int?
            when {
                index == null -> RockNavigator.Direction.Neutral
                index > currentIndex -> direction = RockNavigator.Direction.Forward
                index < currentIndex -> direction = RockNavigator.Direction.Back
                else -> direction = RockNavigator.Direction.Neutral
            }
            currentIndex = index ?: 0
            navigate(window.location.pathname, params, false)
        })
    }

    private val String.asSegments: List<String> get() = split('/').filter { it.isNotBlank() }
    private val _currentScreen = Property(run {
        val path = window.location.pathname
        val args = params
        if (isNavigating)
            throw RedirectException(routes.parse(UrlLikePath(path.asSegments, args))!!)
        window.history.replaceState(currentIndex, "", path)
        window.history.scrollRestoration = ScrollRestoration.MANUAL
        isNavigating = true
        val screen = routes.parse(UrlLikePath(path.asSegments, args)) ?: routes.fallback
        isNavigating = false
        (screen)
    })
    override val currentScreen: Readable<RockScreen>
        get() = SharedReadable { _currentScreen.current }

    private var isNavigating = false
    override var direction: RockNavigator.Direction? = null
        private set
    private fun navigate(path: String, args: Map<String, String>, pushState: Boolean) {
        if (isNavigating)
            throw RedirectException(routes.parse(UrlLikePath(path.asSegments, args))!!)
        if (pushState) {
            currentIndex = nextIndex
            window.history.pushState(
                nextIndex++, "", path
            )
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        } else {
            window.history.replaceState(currentIndex, "", path)
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        }
        isNavigating = true
        val screen = routes.parse(UrlLikePath(path.asSegments, args)) ?: routes.fallback
        isNavigating = false
        _currentScreen set screen
    }
    private fun navigate(rockScreen: RockScreen, pushState: Boolean) {
        if (isNavigating)
            throw RedirectException(rockScreen)
        val path = routes.render(rockScreen)?.segments?.joinToString("/")?.let { "/$it" }
        if (pushState) {
            currentIndex = nextIndex
            window.history.pushState(
                nextIndex++, "", path
            )
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        } else {
            window.history.replaceState(currentIndex, "", path)
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        }
        isNavigating = true
        isNavigating = false
        _currentScreen set rockScreen
    }

    override fun navigate(screen: RockScreen) {
        direction = RockNavigator.Direction.Forward
        navigate(screen, pushState = true)
    }

    override fun replace(screen: RockScreen) {
        direction = RockNavigator.Direction.Neutral
        navigate(screen, pushState = false)
    }

    override fun notifyParamUpdate() {
        window.location.search = routes.render(currentScreen.once)?.parameters?.entries?.joinToString("&") { it.key + "=" + it.value } ?: ""
    }

    override fun goBack() {
        window.history.go(-1)
    }
}
