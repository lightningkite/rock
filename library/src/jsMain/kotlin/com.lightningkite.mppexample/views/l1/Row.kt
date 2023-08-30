package com.lightningkite.mppexample

import org.w3c.dom.HTMLTableRowElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Row = HTMLTableRowElement

actual inline fun ViewContext.row(setup: Row.() -> Unit): Unit = element<HTMLTableRowElement>("div") {
    style.display = "flex"
    style.flexDirection = "row"
    setup()
}

actual var Row.gravity: StackGravity
    get() = throw NotImplementedError()
    set(value) {
        style.alignItems = when (value) {
            StackGravity.Start -> "flex-start"
            StackGravity.Center -> "center"
            StackGravity.End -> "flex-end"
        }
    }
