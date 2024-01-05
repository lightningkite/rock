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

class ExtensionData(
    val reference: WeakReference<*>,
    val map: HashMap<Any, Any?> = HashMap()
) {
    @OptIn(ExperimentalNativeApi::class)
    companion object {
        val all = HashMap<Int, ExtensionData>()
        fun get(item: Any): ExtensionData? = all[item.identityHashCode()]
        fun getOrPut(item: Any): ExtensionData = all.getOrPut(item.identityHashCode()) {
            ExtensionData(WeakReference(item))
        }
        fun clean() {
            all.entries.removeAll { (key, value) ->
                (value.reference.get() == null)
            }
        }
    }
}

class ExtensionProperty<A: NSObject, B>: ReadWriteProperty<A, B?> {
    val myData = HashMap<A, B>(5000)
    override fun getValue(thisRef: A, property: KProperty<*>): B? = getValue(thisRef)
    override fun setValue(thisRef: A, property: KProperty<*>, value: B?) = setValue(thisRef, value)
    fun getValue(thisRef: A): B? = myData[thisRef]
    fun setValue(thisRef: A, value: B?) {
        if(value == null) myData.remove(thisRef)
        else myData[thisRef] = value
    }
}

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