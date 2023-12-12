package com.lightningkite.rock.navigation

import com.lightningkite.rock.FallbackRoute
import com.lightningkite.rock.decodeURIComponent
import com.lightningkite.rock.encodeURIComponent
import com.lightningkite.rock.reactive.*
import kotlinx.browser.window
import org.w3c.dom.Location
import org.w3c.dom.MANUAL
import org.w3c.dom.PopStateEvent
import org.w3c.dom.ScrollRestoration
import org.w3c.dom.url.URLSearchParams

actual class PlatformNavigator actual constructor(
    override val routes: Routes
) : RockNavigator {
    private fun Location.urlLike() = UrlLikePath(
        segments = pathname.split('/').filter { it.isNotBlank() },
        parameters = search.trimStart('?').split('&').filter { it.isNotBlank() }.associate { it.substringBefore('=') to decodeURIComponent(it.substringAfter('=')) }
    )

    override val dialog: RockNavigator = LocalNavigator(routes).also {
        it.stack.value = listOf()
    }
    private var nextIndex: Int = 1
    private val currentIndexProp = Property(0)
    private var currentIndex: Int by currentIndexProp

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
            navigate(window.location.urlLike(), false)
        })
    }

    private val String.asSegments: List<String> get() = split('/').filter { it.isNotBlank() }
    private val _currentScreen = Property(run {
        val path = window.location.urlLike()
        println(path)
        isNavigating = true
        val screen = routes.parse(path) ?: routes.fallback
        isNavigating = false
        (screen)
    })
    override val currentScreen: Readable<RockScreen>
        get() = _currentScreen
    override val canGoBack: Readable<Boolean>
        get() = shared {
            currentIndexProp.await() > 0
        }

    init {
        CalculationContext.NeverEnds.reactiveScope {
            currentScreen.await().let {
                val rendered = routes.render(it)
                rendered?.listenables?.forEach { rerunOn(it) }
                rendered?.urlLikePath?.let {
                    if(window.location.urlLike() != it) {
                        window.history.replaceState(currentIndex, "", it.render())
                    }
                }
            }
        }
    }

    private var isNavigating = false
    override var direction: RockNavigator.Direction? = null
        private set
    private fun navigate(urlLikePath: UrlLikePath, pushState: Boolean) {
        val rockScreen = routes.parse(urlLikePath) ?: routes.fallback
        navigate(urlLikePath, rockScreen, pushState)
    }
    private fun navigate(rockScreen: RockScreen, pushState: Boolean) {
        val path = routes.render(rockScreen)?.urlLikePath ?: return
        navigate(path, rockScreen, pushState)
    }

    private fun navigate(
        path: UrlLikePath,
        rockScreen: RockScreen,
        pushState: Boolean,
    ) {
        if (isNavigating)
            throw RedirectException(rockScreen)
        if (pushState) {
            currentIndex = nextIndex
            window.history.pushState(
                nextIndex++, "", path.render()
            )
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        } else {
            window.history.replaceState(currentIndex, "", path.render())
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        }
        isNavigating = true
        _currentScreen.value = rockScreen
        isNavigating = false
    }

    override fun navigate(screen: RockScreen) {
        direction = RockNavigator.Direction.Forward
        navigate(screen, pushState = true)
    }

    override fun replace(screen: RockScreen) {
        direction = RockNavigator.Direction.Neutral
        navigate(screen, pushState = false)
    }

    override fun goBack() {
        window.history.go(-1)
    }

    override fun dismiss() {
        window.history.go(-1)
    }
}
