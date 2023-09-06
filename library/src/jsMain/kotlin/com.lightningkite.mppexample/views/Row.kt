package com.lightningkite.mppexample

import org.w3c.dom.HTMLTableRowElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Row = HTMLTableRowElement

@ViewDsl
actual inline fun ViewContext.row(setup: Row.() -> Unit): Unit = element<HTMLTableRowElement>("div") {
    style.display = "flex"
    style.flexDirection = "row"
    setup()
}

actual var Row.gravity: RowGravity
    get() = throw NotImplementedError()
    set(value) {
        style.alignItems = when (value) {
            RowGravity.Left -> "flex-start"
            RowGravity.Center -> "center"
            RowGravity.Right -> "flex-end"
        }
    }
