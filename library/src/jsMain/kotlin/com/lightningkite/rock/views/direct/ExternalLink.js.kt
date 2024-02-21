package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLAnchorElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = HTMLAnchorElement

@ViewDsl
actual fun ViewWriter.externalLinkActual(setup: ExternalLink.() -> Unit) =
    themedElementClickable<NExternalLink>("a") {
        classList.add("rock-stack")
        setup(ExternalLink(this))
    }

actual inline var ExternalLink.to: String
    get() = native.href
    set(value) {
        native.href = value
    }
actual inline var ExternalLink.newTab: Boolean
    get() = native.target == "_blank"
    set(value) {
        native.target = if (value) "_blank" else "_self"
    }