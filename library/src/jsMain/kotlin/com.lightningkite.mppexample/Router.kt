package com.lightningkite.mppexample

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.PopStateEvent

actual class RockNavigator actual constructor(
    private val router: Router,
    private val context: ViewContext
) : IRockNavigator {
    private var nextIndex: Int = 0

    init {
        context.run {
            navigate(currentPath, pushState = false, transition = ScreenTransition.None)
        }
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            println("Intercepted navigation to $currentPath")
            context.run {
                navigate(currentPath, pushState = false, transition = ScreenTransition.Pop)
            }
        })
    }

    override var currentPath: String
        get() = window.location.pathname
        set(value) = throw NotImplementedError()

    override fun navigate(path: String, pushState: Boolean, transition: ScreenTransition) {
        println("Navigating to $path")
        val current = document.body?.querySelector("#rock-screen-animate-in") as HTMLElement?

        if (current != null) {
            current.id = "rock-screen-animate-out"
            val exitClass = getExitTransitionClass(transition)
            if (exitClass != null) {
                current.addEventListener("animationend", {
                    current.outerHTML = ""
                })
                current.style.animation = "$exitClass 0.25s"
            }
        }

        if (pushState) window.history.pushState(
            mapOf(
                "path" to path
            ), "", path
        )
        router!!.render(path)
        val newElement = document.body?.querySelector("#rock-screen-animate-in") as HTMLElement?
        if (newElement != null) {
            val enterClass = getEnterTransitionClass(transition)
            if (enterClass != null) {
                newElement.addEventListener("animationend", {
                    newElement.asDynamic().onanimationend = null
                    newElement.style.removeProperty("animation")
                })
                newElement.style.position = "absolute"
                newElement.style.top = "0"
                newElement.style.left = "0"
                newElement.style.right = "0"
                newElement.style.bottom = "0"
                newElement.style.animation = "$enterClass 0.25s"
            }
        }
    }

    private fun getEnterTransitionClass(transition: ScreenTransition): String? {
        return when (transition) {
            ScreenTransition.None -> null
            ScreenTransition.Push -> "stack-push-in"
            ScreenTransition.Pop -> "stack-pop-in"
            ScreenTransition.PullDown -> "stack-pull-down-in"
            ScreenTransition.PullUp -> "stack-pull-up-in"
            ScreenTransition.Fade -> "stack-fade-in"
            ScreenTransition.GrowFade -> "stack-grow-fade-in"
            ScreenTransition.ShrinkFade -> "stack-shrink-fade-in"
        }
    }

    private fun getExitTransitionClass(transition: ScreenTransition): String? {
        return when (transition) {
            ScreenTransition.None -> null
            ScreenTransition.Push -> "stack-push-out"
            ScreenTransition.Pop -> "stack-pop-out"
            ScreenTransition.PullDown -> "stack-pull-down-out"
            ScreenTransition.PullUp -> "stack-pull-up-out"
            ScreenTransition.Fade -> "stack-fade-out"
            ScreenTransition.GrowFade -> "stack-grow-fade-out"
            ScreenTransition.ShrinkFade -> "stack-shrink-fade-out"
        }
    }
}
