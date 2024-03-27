package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NWebView : NView

@JvmInline
value class WebView(override val native: NWebView) : RView<NWebView>

@ViewDsl
expect fun ViewWriter.webViewActual(setup: WebView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.webView(noinline setup: WebView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; webViewActual(setup) }
expect var WebView.url: String
expect var WebView.permitJs: Boolean
expect var WebView.content: String