package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.calculationContext
import org.w3c.dom.HTMLAnchorElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NExternalLink(override val js: HTMLAnchorElement): NView2<HTMLAnchorElement>()

@ViewDsl
actual inline fun ViewWriter.externalLinkActual(crossinline setup: ExternalLink.() -> Unit) =
    themedElementClickable("a", ::NExternalLink) {
        js.classList.add("rock-stack")
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