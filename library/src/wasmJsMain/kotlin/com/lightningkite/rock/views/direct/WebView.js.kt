package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLIFrameElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NWebView(override val js: HTMLIFrameElement): NView2<HTMLIFrameElement>()

@ViewDsl
actual inline fun ViewWriter.webViewActual(crossinline setup: WebView.() -> Unit): Unit =
    themedElement("iframe", ::NWebView) { setup(WebView(this)) }

actual inline var WebView.url: String
    get() = native.js.src
    set(value) {
        native.js.src = value
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