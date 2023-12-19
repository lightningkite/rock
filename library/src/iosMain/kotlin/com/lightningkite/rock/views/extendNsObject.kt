@file:OptIn(ExperimentalNativeApi::class)

package com.lightningkite.rock.views

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.objc.objc_AssociationPolicy
import platform.objc.objc_getAssociatedObject
import platform.objc.objc_setAssociatedObject
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.random.Random
import kotlin.reflect.KProperty

class ExtensionProperty<A: NSObject, B>: ReadWriteProperty<A, B?> {
    @OptIn(ExperimentalForeignApi::class)
    val key = NSValue.valueWithPointer((Random.nextLong().toString() as NSString).UTF8String)
    @OptIn(ExperimentalForeignApi::class)
    override fun getValue(thisRef: A, property: KProperty<*>): B? = com.lightningkite.rock.objc.getAssociatedObjectWithKey(thisRef, key) as? B
    @OptIn(ExperimentalForeignApi::class)
    override fun setValue(thisRef: A, property: KProperty<*>, value: B?) = com.lightningkite.rock.objc.setAssociatedObjectWithKey(thisRef, key, value)
    @OptIn(ExperimentalForeignApi::class)
    fun getValue(thisRef: A): B? = com.lightningkite.rock.objc.getAssociatedObjectWithKey(thisRef, key) as? B
    @OptIn(ExperimentalForeignApi::class)
    fun setValue(thisRef: A, value: B?) = com.lightningkite.rock.objc.setAssociatedObjectWithKey(thisRef, key, value)
}

@OptIn(ExperimentalNativeApi::class)
class ExtensionPropertyWeak<A: NSObject, B: Any>: ReadWriteProperty<A, B?> {
    @OptIn(ExperimentalForeignApi::class)
    val key = NSValue.valueWithPointer((Random.nextLong().toString() as NSString).UTF8String)
    @OptIn(ExperimentalForeignApi::class)
    override fun getValue(thisRef: A, property: KProperty<*>): B? = (com.lightningkite.rock.objc.getAssociatedObjectWithKey(thisRef, key) as? WeakReference<B>)?.value
    @OptIn(ExperimentalForeignApi::class)
    override fun setValue(thisRef: A, property: KProperty<*>, value: B?) = value?.let {
        com.lightningkite.rock.objc.setAssociatedObjectWithKey(thisRef, key, WeakReference(value))
    } ?: run { com.lightningkite.rock.objc.setAssociatedObjectWithKey(thisRef, key, null) }
    @OptIn(ExperimentalForeignApi::class)
    fun getValue(thisRef: A): B? = (com.lightningkite.rock.objc.getAssociatedObjectWithKey(thisRef, key) as? WeakReference<B>)?.value
    @OptIn(ExperimentalForeignApi::class)
    fun setValue(thisRef: A, value: B?) = value?.let {
        com.lightningkite.rock.objc.setAssociatedObjectWithKey(thisRef, key, WeakReference(value))
    } ?: run { com.lightningkite.rock.objc.setAssociatedObjectWithKey(thisRef, key, null) }
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
