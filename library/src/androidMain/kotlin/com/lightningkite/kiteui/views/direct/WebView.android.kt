package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import android.webkit.WebView as AndroidWebView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = AndroidWebView

actual var WebView.url: String
    get() {
        return native.url ?: ""
    }
    set(value) {
        native.loadUrl(value)
    }
actual var WebView.permitJs: Boolean
    get() {
        return native.settings.javaScriptEnabled
    }
    set(value) {
        native.settings.javaScriptEnabled = value
    }
actual var WebView.content: String
    get() {
        return native.tag as? String ?: ""
    }
    set(value) {
        native.tag = value
        native.loadData(value, null, "utf8")
    }

@ViewDsl
actual inline fun ViewWriter.webViewActual(crossinline setup: WebView.() -> Unit) {
    return viewElement(factory = ::AndroidWebView, wrapper = ::WebView, setup = setup)
}