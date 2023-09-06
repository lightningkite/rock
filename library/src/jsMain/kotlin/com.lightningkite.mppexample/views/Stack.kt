package com.lightningkite.mppexample

import org.w3c.dom.HTMLTableRowElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Stack = HTMLTableRowElement

@ViewDsl
actual inline fun ViewContext.stack(setup: Stack.() -> Unit): Unit = element<HTMLTableRowElement>("div") {
    className = "rock-stack"
    setup()
}
