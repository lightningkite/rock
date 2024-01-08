package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NWebView : NView

@JvmInline
value class WebView(override val native: NWebView) : RView<NWebView>

@ViewDsl
expect fun ViewWriter.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.url: String
expect var WebView.permitJs: Boolean
expect var WebView.content: String