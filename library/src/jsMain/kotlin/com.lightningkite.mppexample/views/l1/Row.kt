package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Row = HTMLDivElement

actual inline fun ViewContext.row(setup: Row.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "row"
    setup()
}
