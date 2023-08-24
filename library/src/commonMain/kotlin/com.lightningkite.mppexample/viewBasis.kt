package com.lightningkite.mppexample

@DslMarker
annotation class ViewDsl

@DslMarker
annotation class ViewModifierDsl3

expect class ViewContext {
    val onRemove: OnRemoveHandler
}

expect open class NView
typealias OnRemoveHandler = (onRemove: ()->Unit)->Unit
expect val NView.onRemove: OnRemoveHandler


@ViewDsl expect fun ViewContext.simpleLabel(setup: SimpleLabel.()->Unit = {}): Unit
expect class SimpleLabel: NView
expect var SimpleLabel.text: String

@ViewDsl expect fun ViewContext.column(setup: Column.()->Unit = {}): Unit
expect class Column: NView

@ViewModifierDsl3 expect fun ViewContext.padding(): ViewWrapper