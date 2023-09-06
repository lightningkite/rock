package com.lightningkite.mppexample

import org.w3c.dom.HTMLAnchorElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Link = HTMLAnchorElement

@ViewDsl
actual inline fun ViewContext.link(setup: Link.() -> Unit): Unit = element<HTMLAnchorElement>("a") {
    setup()
}

actual var Link.to: RockScreen
    get() = throw NotImplementedError()
    set(value) {
        this.setAttribute("href", value.createPath())
    }
