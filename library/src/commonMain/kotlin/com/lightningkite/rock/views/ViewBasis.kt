package com.lightningkite.rock.views

import com.lightningkite.rock.models.Angle

@DslMarker
annotation class ViewDsl

@DslMarker
annotation class ViewModifierDsl3

expect class ViewContext {
    val addons: MutableMap<String, Any?>
    val onRemove: OnRemoveHandler
    fun beforeNextElementSetup(action: NView.()->Unit)
    fun afterNextElementSetup(action: NView.()->Unit)
}

expect open class NView()
typealias OnRemoveHandler = (onRemove: () -> Unit) -> Unit

expect val NView.onRemove: OnRemoveHandler
expect var NView.rotation: Angle
expect var NView.alpha: Double
expect var NView.exists: Boolean
expect var NView.visible: Boolean

