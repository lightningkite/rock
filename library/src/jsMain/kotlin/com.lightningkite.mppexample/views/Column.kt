package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Column = HTMLDivElement

@ViewDsl
actual inline fun ViewContext.column(setup: Column.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    setup()
}

actual var Column.gravity: ColumnGravity
    get() = throw NotImplementedError()
    set(value) {
        style.alignItems = when (value) {
            ColumnGravity.Left -> "flex-start"
            ColumnGravity.Center -> "center"
            ColumnGravity.Right -> "flex-end"
        }
    }
