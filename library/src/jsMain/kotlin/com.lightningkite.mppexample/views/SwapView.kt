package com.lightningkite.mppexample


import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias SwapView = HTMLDivElement

@ViewDsl
actual fun ViewContext.swapView(child: Readable<ViewContext.() -> Unit>): Unit {
    box {
        className = "rock-stack"
        style.position = "relative"

        var oldView: HTMLElement? = null

        reactiveScope {
            with(child.current) {
                this?.invoke(this@swapView)
            }
            val newView = lastChild as HTMLElement? ?: return@reactiveScope
            oldView?.let { view ->
                removeChild(view)
            }
            oldView = newView
        }
    }
}
