package com.lightningkite.mppexample


import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias RouterView = HTMLDivElement

const val MAX_REDIRECTS = 3

@ViewDsl
actual fun ViewContext.routerView(router: Router): Unit {
    val theme = this.theme
    box {
        val screen = Property<RockScreen?>(null, overrideDebugName = "Router.screen")
        val reverse = Property(false, overrideDebugName = "Router.reverse")

        var skipTransition = false
        var redirectCount = 0

        navigator = PlatformNavigator(router = router, onScreenChanged = { newScreen, reverseTransition ->
            println("Screen changed to ${newScreen::class.simpleName}")
            reverse set reverseTransition
            screen set newScreen
        })

        className = "rock-stack"
        style.position = "relative"

        val getContext = { derive(this) }
        var oldView: HTMLElement? = null

        reactiveScope {
            val derivedContext = getContext()
            try {
                with(derivedContext) {
                    with(screen.current) {
                        withTheme(theme) {
                            if (this != null) {
                                router.isNavigating = true
                                println("RENDERING ${this::class.simpleName}")
                                render()
                                router.isNavigating = false
                            }
                        }
                    }
                }
            } catch (e: RedirectException) {
                router.isNavigating = false
                redirectCount++
                if (redirectCount >= MAX_REDIRECTS)
                    println("WARNING: Too many redirects. This is likely a bug.")
                skipTransition = true
                screen set e.screen
                return@reactiveScope
            }

            val transition =
                if (skipTransition) ScreenTransition.None else
                    if (reverse.once) derivedContext.screenTransitions.reverse else derivedContext.screenTransitions.forward

            val newView = lastChild as HTMLElement? ?: return@reactiveScope
            newView.classList.add("rock-screen")
            newView.style.animation = "${transition.enterClass()} 0.25s"
            newView.style.marginLeft = "auto"
            newView.style.marginRight = "auto"

            oldView?.let { view ->
                view.style.animation = "${transition.exitClass()} 0.25s"
                view.addEventListener("animationend", {
                    removeChild(view)
                })
            }

            oldView = newView
            skipTransition = false
            redirectCount = 0
        }
    } in fullWidth() in fullHeight()
}

private fun ScreenTransition.enterClass(): String? {
    return when (this) {
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

private fun ScreenTransition.exitClass(): String? {
    return when (this) {
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