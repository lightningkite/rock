package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Column = HTMLDivElement

actual inline fun ViewContext.column(setup: Column.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    setup()
}

actual var Column.gravity: StackGravity
    get() = throw NotImplementedError()
    set(value) {
        style.alignItems = when (value) {
            StackGravity.Start -> "flex-start"
            StackGravity.Center -> "center"
            StackGravity.End -> "flex-end"
        }
    }
