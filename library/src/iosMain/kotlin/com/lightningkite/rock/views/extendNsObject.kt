@file:OptIn(ExperimentalNativeApi::class)

package com.lightningkite.rock.views

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.FontAndStyle
import com.lightningkite.rock.models.SizeConstraints
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.objc.objc_AssociationPolicy
import platform.objc.objc_getAssociatedObject
import platform.objc.objc_setAssociatedObject
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.identityHashCode
import kotlin.native.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.random.Random
import kotlin.reflect.KProperty

class ExtensionProperty<A: NSObject, B>: ReadWriteProperty<A, B?> {
    companion object {
        val storage = HashMap<Any, HashMap<ExtensionProperty<*, *>, Any?>>()
        fun remove(key: Any) = storage.remove(key)
        fun debug() {
            for((key, value) in storage) {
                if(key is UIView) {
                    if(key.window == null) println("Warning! $key is detatched but still holds external storage")
                }
            }
        }
    }
    override fun getValue(thisRef: A, property: KProperty<*>): B? = getValue(thisRef)
    override fun setValue(thisRef: A, property: KProperty<*>, value: B?) = setValue(thisRef, value)
    @Suppress("UNCHECKED_CAST")
    fun getValue(thisRef: A): B? = storage.get(thisRef)?.get(this) as B
    fun setValue(thisRef: A, value: B?) {
        storage.getOrPut(thisRef) { HashMap() }.put(this, value)
    }
}
//class ExtensionProperty<A: NSObject, B>: ReadWriteProperty<A, B?> {
//@OptIn(ExperimentalForeignApi::class)
//override fun getValue(thisRef: A, property: KProperty<*>): B? = getValue(thisRef)
//@OptIn(ExperimentalForeignApi::class)
//override fun setValue(thisRef: A, property: KProperty<*>, value: B?) = setValue(thisRef, value)
//@OptIn(ExperimentalForeignApi::class)
//fun getValue(thisRef: A): B? = com.lightningkite.rock.objc.getAssociatedObjectWithKey(thisRef, key) as? B
//@OptIn(ExperimentalForeignApi::class)
//fun setValue(thisRef: A, value: B?) = com.lightningkite.rock.objc.setAssociatedObjectWithKey(thisRef, key, value)
//}

private val UIViewWeight = ExtensionProperty<UIView, Float>()
var UIView.extensionWeight: Float? by UIViewWeight

private val UIViewMargin = ExtensionProperty<UIView, Double>()
var UIView.extensionMargin: Double? by UIViewMargin

private val UIViewPadding = ExtensionProperty<UIView, Double>()
var UIView.extensionPadding: Double? by UIViewPadding

private val UIViewSizeRules = ExtensionProperty<UIView, SizeConstraints>()
var UIView.extensionSizeConstraints: SizeConstraints? by UIViewSizeRules

private val UIViewHorizontalAlign = ExtensionProperty<UIView, Align>()
var UIView.extensionHorizontalAlign: Align? by UIViewHorizontalAlign

private val UIViewVerticalAlign = ExtensionProperty<UIView, Align>()
var UIView.extensionVerticalAlign: Align? by UIViewVerticalAlign

private val UIViewFontAndStyle = ExtensionProperty<UIView, FontAndStyle>()
var UIView.extensionFontAndStyle: FontAndStyle? by UIViewFontAndStyle

private val UIViewTextSize = ExtensionProperty<UIView, Double>()
var UIView.extensionTextSize: Double? by UIViewTextSize

private val UIViewMarginless = ExtensionProperty<UIView, Boolean>()
var UIView.extensionMarginless: Boolean? by UIViewMarginless

private val UIViewWriter = ExtensionProperty<UIView, ViewWriter>()
var UIView.extensionViewWriter: ViewWriter? by UIViewWriter

private val NSObjectStrongRefHolder = ExtensionProperty<NSObject, NSObject>()
var NSObject.extensionStrongRef: NSObject? by NSObjectStrongRefHolder

val UIViewCalcContext = ExtensionProperty<UIView, NViewCalculationContext>()