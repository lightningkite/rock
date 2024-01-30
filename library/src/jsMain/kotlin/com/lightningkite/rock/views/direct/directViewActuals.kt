package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.pointerevents.PointerEvent

fun ViewWriter.todo(name: String) = element<HTMLSpanElement>("span") {
    innerText = name
}

inline fun <T : HTMLElement> ViewWriter.themedElementPrivateMeta(
    name: String,
    crossinline themeToClass: (Theme) -> String = { DynamicCSS.theme(it) },
    crossinline themeLogic: T.(rootTheme: Boolean, themeChanged: Boolean, virtualClasses: MutableList<String>) -> Unit,
    crossinline setup: T.() -> Unit,
) = element<T>(name) {
    var previousThemeClass: String? = null
    val rootTheme = isRoot
    isRoot = false
    val theme2 = currentTheme
    val transition = transitionNextView
    transitionNextView = ViewWriter.TransitionNextView.No

    calculationContext.reactiveScope {
        val virtualClasses = classList.asList().toMutableList()
        previousThemeClass?.let { virtualClasses.remove(it) }
        val t = theme2()
        val base = themeToClass(t)
        virtualClasses.add(base)
        previousThemeClass = base

        val changed = when(transition) {
            is ViewWriter.TransitionNextView.Maybe -> transition.logic()
            ViewWriter.TransitionNextView.No -> false
            ViewWriter.TransitionNextView.Yes -> true
        }
        themeLogic(rootTheme, changed, virtualClasses)
        className = virtualClasses.joinToString(" ")
    }
    setup()
}

inline fun <T : HTMLElement> ViewWriter.themedElementEditable(name: String, crossinline setup: T.() -> Unit) =
    themedElementPrivateMeta<T>(
        name = name,
        themeToClass = { DynamicCSS.themeInteractive(it) },
        themeLogic = { rootTheme: Boolean, themeChanged: Boolean, virtualClasses: MutableList<String> ->

            if (!themeChanged) {
                virtualClasses.add("sameThemeText")
                virtualClasses.remove("inclBack")
                if (!rootTheme) {
                    virtualClasses.remove("inclBorder")
                }
            } else {
                if (!rootTheme) {
                    virtualClasses.add("inclBorder")
                }
                virtualClasses.remove("sameThemeText")
                virtualClasses.add("inclBack")
            }
        }
    ) {
        classList.add("editable")
        classList.add("inclMargin")
        setup()
    }

inline fun <T : HTMLElement> ViewWriter.themedElementClickable(name: String, crossinline setup: T.() -> Unit) =
    themedElementPrivateMeta<T>(
        name = name,
        themeToClass = { DynamicCSS.themeInteractive(it) },
        themeLogic = { rootTheme: Boolean, themeChanged: Boolean, virtualClasses: MutableList<String> ->

            if (!themeChanged) {
                virtualClasses.remove("inclBack")
                if (!rootTheme) {
                    virtualClasses.remove("inclBorder")
                }
            } else {
                if (!rootTheme) {
                    virtualClasses.add("inclBorder")
                }
                virtualClasses.add("inclBack")
            }
        }
    ) {
        classList.add("clickable")
        classList.add("inclMargin")
        setup()
    }

inline fun <T : HTMLElement> ViewWriter.themedElement(name: String, crossinline setup: T.() -> Unit) = themedElementPrivateMeta<T>(
    name = name,
    themeToClass = { DynamicCSS.theme(it) },
    themeLogic = { rootTheme: Boolean, themeChanged: Boolean, virtualClasses ->
        if (!themeChanged) {
            virtualClasses.remove("inclBack")
            if (!rootTheme) {
                virtualClasses.remove("inclBorder")
            }
        } else {
            if (!rootTheme) {
                virtualClasses.add("inclBorder")
            }
            virtualClasses.add("inclBack")
        }
    }
) {
    classList.add("inclMargin")
    setup()
}

inline fun <T : HTMLElement> ViewWriter.themedElementBackIfChanged(name: String, crossinline setup: T.() -> Unit) =
    themedElementPrivateMeta<T>(
        name = name,
        themeToClass = { DynamicCSS.theme(it) },
        themeLogic = { rootTheme: Boolean, themeChanged: Boolean, virtualClasses ->
            if (!themeChanged) {
                virtualClasses.remove("inclBack")
                virtualClasses.remove("inclBorder")
                if (!rootTheme) {
                    virtualClasses.remove("inclMargin")
                }
            } else {
                if (!rootTheme) {
                    virtualClasses.add("inclBorder")
                    virtualClasses.add("inclMargin")
                }
                virtualClasses.add("inclBack")
            }
        }
    ) {
        setup()
    }

@ViewDsl
internal fun ViewWriter.textElement(elementBase: String, setup: TextView.() -> Unit): Unit =
    themedElement<HTMLDivElement>(elementBase) {
        setup(TextView(this))
        style.whiteSpace = "pre-wrap"
    }

@ViewDsl

internal fun ViewWriter.headerElement(elementBase: String, setup: TextView.() -> Unit): Unit =
    themedElement<HTMLDivElement>(elementBase) {
        setup(TextView(this))
        style.whiteSpace = "pre-wrap"
        classList.add("title")
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
        val b = native.getBoundingClientRect()
        action(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
    }


external class ResizeObserver(callback: (Array<ResizeObserverEntry>, observer: ResizeObserver)->Unit) {
    fun disconnect()
    fun observe(target: Element, options: ResizeObserverOptions = definedExternally)
    fun unobserve(target: Element)
}
external interface ResizeObserverOptions {
    val box: String
}
external interface ResizeObserverEntry {
    val target: Element
    val contentRect: DOMRectReadOnly
    val contentBoxSize: ResizeObserverEntryBoxSize
    val borderBoxSize: ResizeObserverEntryBoxSize
}
external interface ResizeObserverEntryBoxSize {
    val blockSize: Double
    val inlineSize: Double
}

data class SizeReader(val native: HTMLElement, val key: String): Readable<Double> {
    override suspend fun awaitRaw(): Double = native.asDynamic()[key].unsafeCast<Int>().toDouble()
    override fun addListener(listener: () -> Unit): () -> Unit {
        val o = ResizeObserver { _, _ ->
            listener()
        }
        o.observe(native)
        return { o.disconnect() }
    }
}