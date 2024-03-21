package com.lightningkite.rock.views.direct

import com.lightningkite.rock.JsAnyDelegate
import com.lightningkite.rock.JsAnyNativeDelegate
import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.*
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import kotlin.time.Duration

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NSwapView(override val js: HTMLDivElement): NView2<HTMLDivElement>() {
    lateinit var viewWriter: ViewWriter
}

var HTMLElement.__ROCK__removing by JsAnyDelegate<HTMLElement, JsBoolean>("__ROCK__removing")

@ViewDsl
actual inline fun ViewWriter.swapViewActual(crossinline setup: SwapView.() -> Unit): Unit = themedElement("div", ::NSwapView) {
    js.classList.add("rock-swap")
    this.viewWriter = split()
    setup(SwapView(this))
}

@ViewDsl
actual inline fun ViewWriter.swapViewDialogActual(crossinline setup: SwapView.() -> Unit): Unit = themedElement("div", ::NSwapView) {
    js.classList.add("rock-swap")
    js.classList.add("dialog")
    this.viewWriter = split()
    js.hidden = true
    setup(SwapView(this))
}

actual fun SwapView.swap(transition: ScreenTransition, createNewView: ViewWriter.() -> Unit): Unit {
    val vw = native.viewWriter
    val keyframeName = DynamicCSS.transition(transition)
    val previousLast = native.js.lastElementChild
    native.js.children.let { (0 until it.length).map { i -> it.get(i) } }.filterIsInstance<HTMLElement>()
        .forEach { view ->
            if (view.__ROCK__removing?.toBoolean() == true) return@forEach
            view.__ROCK__removing = true.toJsBoolean()
            view.shutdown()
            val myStyle = window.getComputedStyle(native.js)
            val transitionTime = myStyle.transitionDuration.takeUnless { it.isBlank() } ?: "0.15"
            view.style.animation = "${keyframeName}-exit $transitionTime forwards"
            val parent = view.parentElement
            window.setTimeout({
                if (view.parentElement == parent) {
                    native.js.removeChild(view)
                }
                null
            }, 240)
        }
    native.js.withoutAnimation {
        createNewView(vw)
    }
    (native.js.lastElementChild as? HTMLElement).takeUnless { it == previousLast }?.let { newView ->
        newView.classList.add("forcePadding")
        exists = true
        val myStyle = window.getComputedStyle(native.js)
        val transitionTime = myStyle.transitionDuration.takeUnless { it.isBlank() } ?: "0.15"
        newView.style.animation = "${keyframeName}-enter $transitionTime forwards"
    } ?: run {
        exists = false
    }
}