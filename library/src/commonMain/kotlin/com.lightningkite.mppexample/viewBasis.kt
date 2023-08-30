package com.lightningkite.mppexample

@DslMarker
annotation class ViewDsl

@DslMarker
annotation class ViewModifierDsl3

expect class ViewContext {
    val onRemove: OnRemoveHandler
}

expect open class NView()
typealias OnRemoveHandler = (onRemove: ()->Unit)->Unit
expect val NView.onRemove: OnRemoveHandler
expect var NView.rotation: Angle
expect var NView.alpha: Double
expect var NView.elevation: Dimension
expect var NView.exists: Boolean
expect var NView.visible: Boolean
expect var NView.weight: Int

expect open class NViewWithTextStyle : NView
expect fun NViewWithTextStyle.setStyles(styles: TextStyle)

