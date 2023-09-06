package com.lightningkite.mppexample

expect class Stack : NView

@ViewDsl
expect fun ViewContext.stack(setup: Stack.() -> Unit = {}): Unit
