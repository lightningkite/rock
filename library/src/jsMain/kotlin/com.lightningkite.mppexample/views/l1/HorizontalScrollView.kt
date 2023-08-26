package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias HorizontalScrollView = HTMLDivElement

actual inline fun ViewContext.horizontalScrollView(setup: HorizontalScrollView.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "row"
    style.overflowX = "scroll"
    setup()
}
