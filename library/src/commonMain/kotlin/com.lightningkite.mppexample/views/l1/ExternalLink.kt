package com.lightningkite.mppexample

expect class ExternalLink : NView

@ViewDsl
expect fun ViewContext.externalLink(setup: ExternalLink.() -> Unit = {}): Unit

expect var ExternalLink.to: String
