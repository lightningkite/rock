package com.lightningkite.mppexample

@ViewDsl
expect fun ViewContext.column(setup: Column.() -> Unit = {}): Unit
expect class Column : NView

expect var Column.gravity: StackGravity
