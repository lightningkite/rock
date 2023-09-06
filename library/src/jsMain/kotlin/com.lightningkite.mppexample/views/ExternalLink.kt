package com.lightningkite.mppexample

import org.w3c.dom.HTMLBodyElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ExternalLink = HTMLBodyElement

@ViewDsl
actual inline fun ViewContext.externalLink(setup: ExternalLink.() -> Unit): Unit = element<HTMLBodyElement>("a") {
    setup()
}

actual var ExternalLink.to: String
    get() = throw NotImplementedError()
    set(value) {
        this.setAttribute("href", value)
    }
