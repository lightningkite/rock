package com.lightningkite.mppexample


import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias RouterView = HTMLDivElement

actual fun ViewContext.routerView(setup: ViewContext.() -> Router): Unit {
    box {
        val router = setup()
        val screen = Property<RockScreen?>(null)
        val reverse = Property(false)

        navigator = PlatformNavigator(router = router, onScreenChanged = { newScreen, reverseTransition ->
            reverse set reverseTransition
            screen set newScreen
        })

        className = "rock-stack"
        style.position = "relative"

        val derivedContext = derive(this)
        var oldView: HTMLElement? = null

        reactiveScope {
            with(derivedContext) {
                with(screen.current) {
                    if (this != null) render()
                }
            }
            val transition =
                if (reverse.once) derivedContext.screenTransitions.reverse else derivedContext.screenTransitions.forward
            val newView = lastChild as HTMLElement? ?: return@reactiveScope
            newView.classList.add("rock-screen")
            newView.style.animation = "${transition.enterClass()} 0.25s"
            oldView?.let { view ->
                view.style.animation = "${transition.exitClass()} 0.25s"
                view.addEventListener("animationend", {
                    removeChild(view)
                })
            }
            oldView = newView
        }
    }
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