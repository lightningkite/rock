package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ScrollView = HTMLDivElement

actual inline fun ViewContext.scrollView(setup: ScrollView.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    style.overflowY = "scroll"
    setup()
}
