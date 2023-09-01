package com.lightningkite.mppexample


import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import com.lightningkite.mppexample.RockScreen

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias RouterView = HTMLDivElement

actual fun ViewContext.routerView(setup: ViewContext.() -> Router): Unit = element<HTMLDivElement>("div") {
    println("creating routerview")
    id = "rock-router"
//    val screen = Property<RockScreen>(HomeScreen())
//    setup()
}

fun ViewContext.swapView(screen: Readable<RockScreen>) {
    box {
        className = "rock-stack"
        style.position = "relative"

        val derivedContext = derive(this)
        var oldView: HTMLElement? = null
        reactiveScope {
            with(derivedContext) {
                with(screen.current) {
                    render()
                }
            }
            val transition = derivedContext.screenTransitions.forward
            val newView = lastChild as HTMLElement
            newView.id = "rock-screen-animate-in"
            newView.classList.add("rock-screen")

            val enterClass = getEnterTransitionClass(transition)
            newView.style.animation = "$enterClass 0.25s"
            oldView?.let { view ->
                view.id = "rock-screen-animate-out"
                val exitClass = getExitTransitionClass(transition)
                view.style.animation = "$exitClass 0.25s"
                view.addEventListener("animationend", {
                    removeChild(view)
                })
            }
            oldView = newView
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