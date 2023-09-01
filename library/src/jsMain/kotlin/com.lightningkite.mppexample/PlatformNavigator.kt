package com.lightningkite.mppexample

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.PopStateEvent

actual class PlatformNavigator actual constructor(
    private val router: Router,
    private val context: ViewContext
) : RockNavigator {
    private var nextIndex: Int = 0
    private var currentIndex: Int = 0
    private var routerContainer: HTMLElement? = null

    init {
        context.run {
            navigate(currentPath, NavigationOptions(pushState = false, transitions = ScreenTransitions.None))
        }
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            context.run {
                val index = event.state as Int?
                val reverse = index == null || index < currentIndex
                currentIndex = index ?: 0
                navigate(currentPath, NavigationOptions(pushState = false, reverse = reverse))
            }
        })
    }

    override var currentPath: String
        get() = window.location.pathname
        set(value) = throw NotImplementedError()

    private fun navigate(path: String, options: NavigationOptions) {
//        val transitions = options.transitions ?: context.screenTransitions
//        val transition = if (options.reverse) transitions.reverse else transitions.forward
//
//        val current = document.body?.querySelector("#rock-screen-animate-in") as HTMLElement?
//
//        if (current != null) {
//            current.id = "rock-screen-animate-out"
//            val exitClass = getExitTransitionClass(transition)
//            if (exitClass != null) {
//                current.addEventListener("animationend", {
//                    current.outerHTML = ""
//                })
//                current.style.animation = "$exitClass 0.25s"
//            } else {
//                current.outerHTML = ""
//            }
//        }
//
//        if (options.pushState) {
//            currentIndex = nextIndex
//            window.history.pushState(
//                nextIndex++, "", path
//            )
//        }
//        val creator = router.render(path)
//        context.run {
//            if (routerContainer == null) {
//                routerContainer = this.stack.last()
//                println("got new router container: ${(routerContainer as HTMLElement?)?.id}")
//                box {
//                    id = "rock-screen-animate-in"
//                    creator()
//                }
//            } else {
//                println("add to stack")
//                popCount = 0
//                this.stack.add(routerContainer!!)
//                element(routerContainer!!) {
//                    box {
//                        id = "rock-screen-animate-in"
//                        creator()
//                    }
//                }
//            }
//        }
//        val newElement = document.body?.querySelector("#rock-screen-animate-in") as HTMLElement?
//        if (newElement != null) {
//
//            val enterClass = getEnterTransitionClass(transition)
//            newElement.style.position = "absolute"
//            newElement.style.top = "0"
//            newElement.style.left = "0"
//            newElement.style.right = "0"
//            newElement.style.bottom = "0"
//            if (enterClass != null) {
//                newElement.addEventListener("animationend", {
//                    newElement.asDynamic().onanimationend = null
//                    newElement.style.removeProperty("animation")
//                })
//                newElement.style.animation = "$enterClass 0.25s"
//            }
//        }
    }

    override fun navigate(screen: RockScreen, options: NavigationOptions) = navigate(screen.createPath(), options)

}
