package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.events.Event

fun ViewContext.todo(name: String) = element<HTMLSpanElement>("span") {
    innerText = name
}

inline fun <T : HTMLElement> ViewContext.themedElementInteractive(name: String, setup: T.() -> Unit) = element<T>(name) {
    classList.add("interactive")
    var previousThemeClass: String? = null
    val rootTheme = themeStack.size == 1
    val themeGetter = themeStack.lastOrNull()
    val themeChanged = themeJustChanged
    themeJustChanged = false
    classList.add("inclMargin")

    if (themeGetter != null) {
        reactiveScope {
            previousThemeClass?.let { classList.remove(it) }
            val t = themeGetter()
            val base = DynamicCSS.themeInteractive(t)
            classList.add(base)
            previousThemeClass = base

            if (rootTheme) classList.add("rootTheme")

            if (!themeChanged) {
                classList.remove("inclBack")
                if (!rootTheme) {
                    classList.remove("inclBorder")
                }
            } else {
                if (!rootTheme) {
                    classList.add("inclBorder")
                }
                classList.add("inclBack")
            }
        }
    }
    setup()
}

inline fun <T : HTMLElement> ViewContext.themedElement(name: String, setup: T.() -> Unit) = element<T>(name) {
    var previousThemeClass: String? = null
    val rootTheme = themeStack.size == 1
    val themeGetter = themeStack.lastOrNull()
    val themeChanged = themeJustChanged
    themeJustChanged = false
    classList.add("inclMargin")

    if (themeGetter != null) {
        reactiveScope {
            previousThemeClass?.let { classList.remove(it) }
            val t = themeGetter()
            val base = DynamicCSS.theme(t)
            classList.add(base)
            previousThemeClass = base

            if (rootTheme) classList.add("rootTheme")

            if (!themeChanged) {
                classList.remove("inclBack")
                if (!rootTheme) {
                    classList.remove("inclBorder")
                }
            } else {
                if (!rootTheme) {
                    classList.add("inclBorder")
                }
                classList.add("inclBack")
            }
        }
    }
    setup()
}

inline fun <T : HTMLElement> ViewContext.themedElementBackIfChanged(name: String, setup: T.() -> Unit) =
    element<T>(name) {
        var previousThemeClass: String? = null
        val rootTheme = themeStack.size == 1
        val themeGetter = themeStack.lastOrNull()
        val themeChanged = themeJustChanged
        themeJustChanged = false
        if (themeGetter != null) {
            reactiveScope {
                previousThemeClass?.let { classList.remove(it) }
                val t = themeGetter()
                val base = DynamicCSS.theme(t)
                classList.add(base)
                previousThemeClass = base

                if (rootTheme) classList.add("rootTheme")

                if (!themeChanged) {
                    classList.remove("inclBack")
                    classList.remove("inclBorder")
                    if (!rootTheme) {
                        classList.remove("inclMargin")
                    }
                } else {
                    if (!rootTheme) {
                        classList.add("inclBorder")
                        classList.add("inclMargin")
                    }
                    classList.add("inclBack")
                }
            }
        }
        setup()
    }

inline fun <T : HTMLElement, V> T.vprop(
    eventName: String,
    crossinline get: T.() -> V,
    crossinline set: T.(V) -> Unit
): Writable<V> {
    return object : Writable<V> {
        override fun set(value: V): Unit = set(value)
        override val once: V get() = get()

        override fun addListener(listener: () -> Unit): () -> Unit {
            val callback: (Event) -> Unit = { listener() }
            this@vprop.addEventListener(eventName, callback)
            return { this@vprop.removeEventListener(eventName, callback) }
        }

    }
}

inline val ToggleButton.ToggleButton_inputElement: HTMLInputElement get() = this.children.get(0) as HTMLInputElement

@ViewDsl
internal fun ViewContext.textElement(elementBase: String, setup: TextView.() -> Unit): Unit =
    themedElement<HTMLDivElement>(elementBase) {
        setup()
        style.whiteSpace = "pre-wrap"
    }
@ViewDsl

internal fun ViewContext.headerElement(elementBase: String, setup: TextView.() -> Unit): Unit =
    themedElement<HTMLDivElement>(elementBase) {
        setup()
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