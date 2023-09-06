package com.lightningkite.mppexample

expect class Column : NView

@ViewDsl
expect fun ViewContext.column(setup: Column.() -> Unit = {}): Unit

expect var Column.gravity: ColumnGravity

enum class ColumnGravity {
    Top, Center, Bottom
}
