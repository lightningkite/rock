package com.lightningkite.mppexample

expect class Link : NView

@ViewDsl
expect fun ViewContext.link(setup: Link.() -> Unit = {}): Unit

expect var Link.to: RockScreen
expect var Link.content: String
