@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views

import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*

//inline fun <T : HTMLElement> ViewWriter.containsNext(name: String, noinline setup: T.() -> Unit): ViewWrapper =
//    wrapNext<T>(document.createElement(name) as T, setup)
//
//inline fun <T : HTMLElement> ViewWriter.element(name: String, noinline setup: T.() -> Unit) =
//    element(document.createElement(name) as T, setup)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NView = UIView

private class RemovedDetectorView: UIView(CGRectZero.readValue()) {
    var onRemove = ArrayList<()->Unit>()
    var hasBeenActive = false

    init {
        super.setHidden(true)
        super.setUserInteractionEnabled(false)
    }

    override fun willMoveToWindow(newWindow: UIWindow?) {
        super.willMoveToWindow(newWindow)
        super.setHidden(true)
    }

    override fun didMoveToWindow() {
        if (window != null) {
            hasBeenActive = true
        } else if (hasBeenActive) {
            hasBeenActive = false
            onRemove.forEach { it() }
            onRemove = ArrayList()
        }
    }

    override fun setHidden(hidden: Boolean) {
        // no, frick you, you can't see me
        if(!hidden) return
        super.setHidden(hidden)
    }


    override fun setUserInteractionEnabled(userInteractionEnabled: Boolean) {
        // no, frick you, you can't talk to me
        if(userInteractionEnabled) return
        super.setUserInteractionEnabled(userInteractionEnabled)
    }
}

private val UIView.removedDetectorView: RemovedDetectorView
    get() {
        return subviews.asSequence().mapNotNull { it as? RemovedDetectorView }.firstOrNull() ?: run {
            val newOne = RemovedDetectorView()
            addSubview(newOne)
            newOne
        }
    }

data class NViewCalculationContext(val native: NView): CalculationContext {
    override fun onRemove(action: () -> Unit) {
        native.removedDetectorView.onRemove.add(action)
    }

    override fun notifyStart() {
    }

    override fun notifySuccess() {
    }

    override fun notifyFailure(t: Throwable) {
        super.notifyFailure(t)
    }
}
actual val NView.calculationContext: CalculationContext
    get() = NViewCalculationContext(this)

actual var NView.exists: Boolean
    get() = throw NotImplementedError()
    set(value) {
        hidden = !value
    }

actual var NView.visible: Boolean
    get() = throw NotImplementedError()
    set(value) {
        alpha = if (value) 1.0 else 0.0
    }

actual var NView.opacity: Double
    get() = throw NotImplementedError()
    set(value) {
        alpha = value
    }

actual var NView.nativeRotation: Angle
    get() = throw NotImplementedError()
    set(value) {
        TODO()
//        style.transform = "rotate(${value.turns}turn)"
    }

actual fun NView.clearChildren() {
    this.subviews.toList().forEach {
        if(it is RemovedDetectorView) return@forEach
        (it as? UIView)?.removeFromSuperview()
    }
}
actual fun NView.addChild(child: NView) {
//    child.setTranslatesAutoresizingMaskIntoConstraints(false)
    this.addSubview(child)
}
