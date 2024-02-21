package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLIFrameElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = HTMLIFrameElement

@ViewDsl
actual fun ViewWriter.webViewActual(setup: WebView.() -> Unit): Unit =
    themedElement<NWebView>("iframe") { setup(WebView(this)) }

actual inline var WebView.url: String
    get() = native.src
    set(value) {
        native.src = value
    }
actual inline var WebView.permitJs: Boolean
    get() = TODO()
    set(value) {
        TODO()
    }
actual inline var WebView.content: String
    get() = TODO()
    set(value) {
        TODO()
    }