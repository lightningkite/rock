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

inline fun <T : HTMLElement> ViewContext.themedElementPrivateMeta(
    name: String,
    crossinline themeToClass: (Theme)->String = { DynamicCSS.theme(it) },
    crossinline themeLogic: T.(rootTheme: Boolean, themeChanged: Boolean) -> Unit,
    setup: T.() -> Unit,
) = element<T>(name) {
    var previousThemeClass: String? = null
    val rootTheme = themeStack.size == 1
    val themeGetter = themeStack.lastOrNull()
    val themeChanged = themeJustChanged
    themeJustChanged = false

    if (themeGetter != null) {
        reactiveScope {
            previousThemeClass?.let { classList.remove(it) }
            val t = themeGetter()
            val base = themeToClass(t)
            classList.add(base)
            previousThemeClass = base

            themeLogic(rootTheme, themeChanged)
        }
    }
    setup()
}

inline fun <T : HTMLElement> ViewContext.themedElementEditable(name: String, setup: T.() -> Unit) = themedElementPrivateMeta<T>(
    name = name,
    themeToClass = { DynamicCSS.themeInteractive(it) },
    themeLogic = { rootTheme: Boolean, themeChanged: Boolean ->

        if (!themeChanged) {
            classList.add("sameThemeText")
            classList.remove("inclBack")
            if (!rootTheme) {
                classList.remove("inclBorder")
            }
        } else {
            if (!rootTheme) {
                classList.add("inclBorder")
            }
            classList.remove("sameThemeText")
            classList.add("inclBack")
        }
    }
) {
    classList.add("editable")
    classList.add("inclMargin")
    setup()
}

inline fun <T : HTMLElement> ViewContext.themedElementClickable(name: String, setup: T.() -> Unit) = themedElementPrivateMeta<T>(
    name = name,
    themeToClass = { DynamicCSS.themeInteractive(it) },
    themeLogic = { rootTheme: Boolean, themeChanged: Boolean ->

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
) {
    classList.add("clickable")
    classList.add("inclMargin")
    setup()
}

inline fun <T : HTMLElement> ViewContext.themedElement(name: String, setup: T.() -> Unit) = themedElementPrivateMeta<T>(
    name = name,
    themeToClass = { DynamicCSS.theme(it) },
    themeLogic = { rootTheme: Boolean, themeChanged: Boolean ->
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
) {
    classList.add("inclMargin")
    setup()
}

inline fun <T : HTMLElement> ViewContext.themedElementBackIfChanged(name: String, setup: T.() -> Unit) = themedElementPrivateMeta<T>(
    name = name,
    themeToClass = { DynamicCSS.theme(it) },
    themeLogic = { rootTheme: Boolean, themeChanged: Boolean ->
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
) {
    setup()
}

inline fun <T : HTMLElement, V> T.vprop(
    eventName: String,
    crossinline get: T.() -> V,
    crossinline set: T.(V) -> Unit
): Writable<V> {
    return object : Writable<V> {
        override fun set(value: V): Unit {
            set(this@vprop, value)
        }
        override val once: V get() = get()
        private var block = false

        override fun addListener(listener: () -> Unit): () -> Unit {
            val callback: (Event) -> Unit = { listener() }
            this@vprop.addEventListener(eventName, callback)
            return { this@vprop.removeEventListener(eventName, callback) }
        }

    }
}

@ViewDsl
internal fun ViewContext.textElement(elementBase: String, setup: TextView.() -> Unit): Unit =
    themedElement<HTMLDivElement>(elementBase) {
        setup(TextView(this))
        style.whiteSpace = "pre-wrap"
    }
@ViewDsl

internal fun ViewContext.headerElement(elementBase: String, setup: TextView.() -> Unit): Unit =
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