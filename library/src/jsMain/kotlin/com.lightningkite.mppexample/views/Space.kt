package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Space = HTMLDivElement

actual inline fun ViewContext.space(setup: Space.() -> Unit): Unit = element<HTMLDivElement>("div") {
    setup()
}

actual var Space.size: SizeConstraints
    get() = throw NotImplementedError()
    set(constraints) {
        if (constraints.minHeight == null)
            style.removeProperty("minHeight")
        else
            style.minHeight = constraints.minHeight.value

        if (constraints.maxHeight == null)
            style.removeProperty("maxHeight")
        else
            style.maxHeight = constraints.maxHeight.value

        if (constraints.minWidth == null)
            style.removeProperty("minWidth")
        else
            style.minWidth = constraints.minWidth.value

        if (constraints.maxWidth == null)
            style.removeProperty("maxWidth")
        else
            style.maxWidth = constraints.maxWidth.value

        if (constraints.width == null)
            style.removeProperty("width")
        else
            style.width = constraints.width.value

        if (constraints.height == null)
            style.removeProperty("height")
        else
            style.height = constraints.height.value
    }
