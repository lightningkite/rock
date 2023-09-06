package com.lightningkite.mppexample

expect class Row : NView

@ViewDsl
expect fun ViewContext.row(setup: Row.() -> Unit = {}): Unit

expect var Row.gravity: RowGravity

enum class RowGravity {
    Left, Center, Right
}
