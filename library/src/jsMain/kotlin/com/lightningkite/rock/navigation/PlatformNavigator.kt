package com.lightningkite.rock.navigation

import com.lightningkite.rock.FallbackRoute
import com.lightningkite.rock.decodeURIComponent
import com.lightningkite.rock.encodeURIComponent
import com.lightningkite.rock.reactive.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import org.w3c.dom.url.URLSearchParams

external interface BaseUrlScript { val baseUrl: String }

actual object PlatformNavigator : RockNavigator {
    var basePath = (document.getElementById("baseUrlLocation") as? HTMLScriptElement)?.innerText?.let {
        JSON.parse<BaseUrlScript>(it).baseUrl
    } ?: "/"

    private lateinit var _routes: Routes
    actual override var routes: Routes
        get() = _routes
        set(value) {
            _routes = value
            if(_currentScreen.value is RockScreen.Empty) {
                _currentScreen.value = routes.parse(window.location.urlLike()) ?: routes.fallback
            }
            CalculationContext.NeverEnds.reactiveScope {
                currentScreen.await().let {
                    val rendered = value.render(it)
                    rendered?.listenables?.forEach { rerunOn(it) }
                    rendered?.urlLikePath?.let {
                        if(window.location.urlLike() != it) {
                            window.history.replaceState(currentIndex, "", basePath + it.render())
                        }
                    }
                }
            }
        }

    private fun Location.urlLike() = UrlLikePath(
        segments = pathname.removePrefix(basePath).split('/').filter { it.isNotBlank() },
        parameters = search.trimStart('?').split('&').filter { it.isNotBlank() }.associate { it.substringBefore('=') to decodeURIComponent(it.substringAfter('=')) }
    )

    override val dialog: RockNavigator = LocalNavigator({ routes })

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
    private val _currentScreen = Property<RockScreen>(RockScreen.Empty)
    override val currentScreen: Readable<RockScreen>
        get() = _currentScreen
    override val canGoBack: Readable<Boolean>
        get() = shared {
            currentIndexProp.await() > 0
        }

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
        if (pushState) {
            currentIndex = nextIndex
            window.history.pushState(
                nextIndex++, "", (basePath + path.render()).also { println("pushing state $it") }
            )
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        } else {
            window.history.replaceState(currentIndex, "", basePath + path.render())
            window.history.scrollRestoration = ScrollRestoration.MANUAL
        }
        _currentScreen.value = rockScreen
    }

    override fun navigate(screen: RockScreen) {
        direction = RockNavigator.Direction.Forward
        navigate(screen, pushState = true)
    }

    override fun replace(screen: RockScreen) {
        direction = RockNavigator.Direction.Neutral
        navigate(screen, pushState = false)
    }

    override fun reset(screen: RockScreen) {
        direction = RockNavigator.Direction.Neutral
        navigate(screen, pushState = false)
    }

    override fun goBack(): Boolean {
        window.history.go(-1)
        return true
    }

    override fun dismiss(): Boolean {
        window.history.go(-1)
        return true
    }
}
