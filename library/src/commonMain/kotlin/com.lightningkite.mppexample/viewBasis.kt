package com.lightningkite.mppexample

import org.w3c.dom.Navigator
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@DslMarker
annotation class ViewDsl

@DslMarker
annotation class ViewModifierDsl3

expect class ViewContext {
    val addons: MutableMap<String, Any?>
    val onRemove: OnRemoveHandler
}

expect open class NView()
typealias OnRemoveHandler = (onRemove: () -> Unit) -> Unit

expect val NView.onRemove: OnRemoveHandler
expect var NView.rotation: Angle
expect var NView.alpha: Double
expect var NView.elevation: Dimension
expect var NView.exists: Boolean
expect var NView.visible: Boolean
expect var NView.id: String

expect open class NViewWithTextStyle : NView

expect fun NViewWithTextStyle.setStyles(styles: TextStyle)

@Suppress("UNCHECKED_CAST")
fun <T> viewContextAddon(init: T): ReadWriteProperty<ViewContext, T> = object : ReadWriteProperty<ViewContext, T> {
    override fun getValue(thisRef: ViewContext, property: KProperty<*>): T =
        thisRef.addons.getOrPut(property.name) { init } as T

    override fun setValue(thisRef: ViewContext, property: KProperty<*>, value: T) {
        println("CHANGING ADDON")
        thisRef.addons[property.name] = value
    }
}

//var ViewContext.defaultSpacing by viewContextAddon(2)
//val ViewContext.defaultSpacing2 by viewContextAddon(Property(1))
var ViewContext.navigator by viewContextAddon<IRockNavigator>(DummyRockNavigator())
var ViewContext.screenTransitions by viewContextAddon(ScreenTransitions.PushPop)
