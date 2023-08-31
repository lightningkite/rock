package com.lightningkite.mppexample


expect class Space : NView

expect fun ViewContext.space(setup: Space.() -> Unit = {}): Unit
expect var Space.size: SizeConstraints
