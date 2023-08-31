package com.lightningkite.mppexample


expect class WebView : NView

expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.url: String
