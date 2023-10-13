package com.lightningkite.rock.navigation

import com.lightningkite.rock.navigation.RedirectException
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.navigation.Router
import kotlinx.browser.window
import org.w3c.dom.MANUAL
import org.w3c.dom.PopStateEvent
import org.w3c.dom.ScrollRestoration

actual class PlatformNavigator actual constructor(
    override val router: Router, val onScreenChanged: (RockScreen, Boolean) -> Unit
) : RockNavigator {
    private var nextIndex: Int = 1
    private var currentIndex: Int = 0

    init {
        navigate("${currentPath}${currentSearch}", reverse = false, pushState = false)
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            val index = event.state as Int?
            val reverse = index == null || index < currentIndex
            currentIndex = index ?: 0
            navigate(
                "${currentPath}${currentSearch}", reverse = reverse, pushState = false
            )
        })
    }

    override var currentPath: String
        get() = window.location.pathname
        set(value) = throw NotImplementedError()

    var currentSearch: String
        get() = window.location.search
        set(value) = throw NotImplementedError()

    private fun navigate(path: String, reverse: Boolean, pushState: Boolean) {
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
        val screen = router.findScreen(path)
        if (router.isNavigating)
            throw RedirectException(screen)
        onScreenChanged(screen, reverse)
    }

    override fun navigate(screen: RockScreen) = navigate(screen.createPath(), reverse = false, pushState = true)

    override fun replace(screen: RockScreen) = navigate(screen.createPath(), reverse = false, pushState = false)

    override fun goBack() = window.history.go(-1)
}
