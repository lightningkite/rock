package com.lightningkite.mppexample

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.PopStateEvent

actual class PlatformNavigator actual constructor(
    val router: Router,
    val onScreenChanged: (RockScreen, Boolean) -> Unit
) : RockNavigator {
    private var nextIndex: Int = 1
    private var currentIndex: Int = 0

    init {
        navigate(currentPath, reverse = false, pushState = false)
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            val index = event.state as Int?
            val reverse = index == null || index < currentIndex
            currentIndex = index ?: 0
            navigate(currentPath, reverse = reverse, pushState = false)
        })
    }

    override var currentPath: String
        get() = window.location.pathname
        set(value) = throw NotImplementedError()

    private fun navigate(path: String, reverse: Boolean, pushState: Boolean) {
        if (pushState) {
            currentIndex = nextIndex
            window.history.pushState(
                nextIndex++, "", path
            )
        } else {
            window.history.replaceState(currentIndex, "", path)
        }

        val screen = router.findRoute(path)
        onScreenChanged(screen, reverse)
    }

    override fun navigate(screen: RockScreen) = navigate(screen.createPath(), reverse = false, pushState = true)

    override fun replace(screen: RockScreen) = navigate(screen.createPath(), reverse = false, pushState = false)
}
