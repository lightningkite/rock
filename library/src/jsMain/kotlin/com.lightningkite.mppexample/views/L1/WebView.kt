package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLIFrameElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias WebView = HTMLIFrameElement

actual inline fun ViewContext.webView(setup: WebView.() -> Unit): Unit = element<HTMLIFrameElement>("iframe", setup)

actual var WebView.url: String
    get() = throw NotImplementedError()
    set(value) {
        src = value
    }
