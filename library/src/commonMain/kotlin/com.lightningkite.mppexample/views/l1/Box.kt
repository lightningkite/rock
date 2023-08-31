package com.lightningkite.mppexample


expect class Box : NView

expect fun ViewContext.box(setup: Box.() -> Unit = {}): Unit
