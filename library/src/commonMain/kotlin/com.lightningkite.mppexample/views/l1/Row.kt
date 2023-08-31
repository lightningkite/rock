package com.lightningkite.mppexample

@ViewDsl
expect fun ViewContext.row(setup: Row.() -> Unit = {}): Unit
expect class Row : NView

expect var Row.gravity: StackGravity
