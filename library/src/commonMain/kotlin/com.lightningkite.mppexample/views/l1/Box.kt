package com.lightningkite.mppexample

expect class Box : NView

@ViewDsl
expect fun ViewContext.box(setup: Box.() -> Unit = {}): Unit
