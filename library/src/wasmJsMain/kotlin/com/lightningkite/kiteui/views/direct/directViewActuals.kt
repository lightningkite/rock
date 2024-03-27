package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.get
import com.lightningkite.kiteui.models.*
import com.lightningkite.kiteui.reactive.Readable
import com.lightningkite.kiteui.reactive.ReadableState
import com.lightningkite.kiteui.reactive.reactiveScope
import com.lightningkite.kiteui.views.*
import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.pointerevents.PointerEvent

inline fun <T : HTMLElement, N: NView2<T>> ViewWriter.themedElementEditable(name: String, wrapperConstructor: (T)->N, crossinline setup: N.() -> Unit) = themedElement(name, wrapperConstructor) {
    js.classList.add("editable")
    setup(this)
}

inline fun <T : HTMLElement, N: NView2<T>> ViewWriter.themedElementClickable(name: String, wrapperConstructor: (T)->N, crossinline setup: N.() -> Unit) = themedElement(name, wrapperConstructor, isControl = true) {
    js.classList.add("clickable")
    setup(this)
}

inline fun <T : HTMLElement, N: NView2<T>> ViewWriter.themedElement(name: String, wrapperConstructor: (T)->N, viewDraws: Boolean = true, isControl: Boolean = false, crossinline setup: N.() -> Unit) {
    element<T, N>(name, wrapperConstructor) {
        handleTheme(this, viewDraws = viewDraws, isControl = isControl)
        setup(this)
    }
}

inline fun <T : HTMLElement, N: NView2<T>> ViewWriter.themedElementBackIfChanged(name: String, wrapperConstructor: (T)->N, viewDraws: Boolean = true, crossinline setup: N.() -> Unit) = themedElement(name, wrapperConstructor, viewDraws = false, setup = setup)

@ViewDsl
inline fun ViewWriter.textElement(elementBase: String, crossinline setup: TextView.() -> Unit): Unit =
    element(elementBase, ::NTextView) {
        handleTheme(this, true)
        setup(TextView(this))
        js.style.whiteSpace = "pre-wrap"
    }

@ViewDsl
inline fun ViewWriter.headerElement(elementBase: String, crossinline setup: TextView.() -> Unit): Unit =
    element(elementBase, ::NTextView) {
        js.classList.add("title")
        handleTheme(this, true)
        setup(TextView(this))
        js.style.whiteSpace = "pre-wrap"
    }

fun HTMLElement.__resetContentToOptionList(options: List<WidgetOption>, selected: String) {
    innerHTML = ""
    for (item in options) appendChild((document.createElement("option") as HTMLOptionElement).apply {
        this.value = item.key
        this.innerText = item.display
        this.selected = item.key == selected
    })
}

internal fun Canvas.pointerListenerHandler(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): (Event) -> Unit =
    {
        val event = it as PointerEvent
        val b = native.js.getBoundingClientRect()
        action(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
    }


external class ResizeObserver(callback: (JsArray<ResizeObserverEntry>, observer: ResizeObserver)->Unit): JsAny {
    fun disconnect()
    fun observe(target: Element, options: ResizeObserverOptions = definedExternally)
    fun unobserve(target: Element)
}
external interface ResizeObserverOptions: JsAny {
    val box: String
}
external interface ResizeObserverEntry: JsAny {
    val target: Element
    val contentRect: DOMRectReadOnly
    val contentBoxSize: ResizeObserverEntryBoxSize
    val borderBoxSize: ResizeObserverEntryBoxSize
}
external interface ResizeObserverEntryBoxSize: JsAny {
    val blockSize: Double
    val inlineSize: Double
}

data class SizeReader(val native: HTMLElement, val key: String): Readable<Double> {
    override val state: ReadableState<Double>
        get() = ReadableState(native[key]?.unsafeCast<JsNumber>()?.toDouble() ?: 0.0)
    override fun addListener(listener: () -> Unit): () -> Unit {
        val o = ResizeObserver { _, _ ->
            listener()
        }
        o.observe(native)
        return { o.disconnect() }
    }
}