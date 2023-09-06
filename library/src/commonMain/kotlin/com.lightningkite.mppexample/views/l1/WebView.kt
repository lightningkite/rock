package com.lightningkite.mppexample

expect class WebView : NView

@ViewDsl
expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit

expect var WebView.url: String
