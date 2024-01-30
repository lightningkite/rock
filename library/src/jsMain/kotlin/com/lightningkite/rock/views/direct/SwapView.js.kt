package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.*
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = HTMLDivElement

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit): Unit = themedElement<NSwapView>("div") {
    classList.add("rock-swap")
    this.asDynamic().__ROCK_ViewWriter__ = split()
    setup(SwapView(this))
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit): Unit = themedElement<NSwapView>("div") {
    classList.add("rock-swap")
    classList.add("dialog")
    this.asDynamic().__ROCK_ViewWriter__ = split()
    hidden = true
    setup(SwapView(this))
}

actual fun SwapView.swap(transition: ScreenTransition, createNewView: ViewWriter.() -> Unit): Unit {
    val vw = native.asDynamic().__ROCK_ViewWriter__ as ViewWriter
    val keyframeName = DynamicCSS.transition(transition)
    val previousLast = native.lastElementChild
    native.children.let { (0 until it.length).map { i -> it.get(i) } }.filterIsInstance<HTMLElement>()
        .forEach { view ->
            if (view.asDynamic().__ROCK__removing) return@forEach
            view.asDynamic().__ROCK__removing = true
            view.style.animation = "${keyframeName}-exit 0.25s forwards"
            val parent = view.parentElement
            window.setTimeout({
                if (view.parentElement == parent) {
                    native.removeChild(view)
                }
            }, 240)
        }
    native.withoutAnimation {
        createNewView(vw)
    }
    (native.lastElementChild as? HTMLElement).takeUnless { it == previousLast }?.let { newView ->
        native.hidden = false
        newView.style.animation = "${keyframeName}-enter 0.25s forwards"
        newView.style.marginLeft = "auto"
        newView.style.marginRight = "auto"
    } ?: run {
        native.hidden = true
    }
}