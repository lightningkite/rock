package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.launchManualCancel
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.calculationContext
import org.w3c.dom.HTMLAnchorElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NExternalLink(override val js: HTMLAnchorElement): NView2<HTMLAnchorElement>()

@ViewDsl
actual inline fun ViewWriter.externalLinkActual(crossinline setup: ExternalLink.() -> Unit) =
    themedElementClickable("a", ::NExternalLink) {
        js.classList.add("kiteui-stack")
        setup(ExternalLink(this))
    }

actual inline var ExternalLink.to: String
    get() = native.js.href
    set(value) {
        native.js.href = value
    }
actual inline var ExternalLink.newTab: Boolean
    get() = native.js.target == "_blank"
    set(value) {
        native.js.target = if (value) "_blank" else "_self"
    }
actual fun ExternalLink.onNavigate(action: suspend () -> Unit): Unit {
    native.js.onclick = {
        calculationContext.launchManualCancel(action)
    }
}