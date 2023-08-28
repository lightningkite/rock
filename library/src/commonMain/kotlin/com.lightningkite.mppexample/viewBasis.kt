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

expect open class NViewWithTextStyle : NView
expect fun NViewWithTextStyle.setStyles(styles: TextStyle)

