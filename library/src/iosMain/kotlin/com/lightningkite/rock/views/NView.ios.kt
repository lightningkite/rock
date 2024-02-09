package com.lightningkite.rock.views

import com.lightningkite.rock.Cancellable
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.reactive.CalculationContext
import com.lightningkite.rock.reactive.Property
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.CoreGraphics.CGAffineTransformRotate
import platform.UIKit.*
import platform.darwin.NSObject
import platform.objc.sel_registerName
import kotlin.experimental.ExperimentalNativeApi

actual fun NView.removeNView(child: NView) {
    child.removeFromSuperview()
    child.shutdown()
}

actual fun NView.listNViews(): List<NView> {
    return subviews.map { it as UIView }
}

var animationsEnabled: Boolean = true
actual inline fun NView.withoutAnimation(action: () -> Unit) {
    if (!animationsEnabled) {
        action()
        return
    }
    try {
        animationsEnabled = false
        action()
    } finally {
        animationsEnabled = true
    }
}

inline fun animateIfAllowed(crossinline action: () -> Unit) {
    if (animationsEnabled) UIView.animateWithDuration(0.15) {
        action()
    } else {
        action()
    }
}

actual fun NView.scrollIntoView(
    horizontal: Align?,
    vertical: Align?,
    animate: Boolean
) {
}

@OptIn(ExperimentalForeignApi::class)
actual fun NView.consumeInputEvents() {
    val actionHolder = object : NSObject() {
        @ObjCAction
        fun eventHandler() {
        }
    }
    val rec = UITapGestureRecognizer(actionHolder, sel_registerName("eventHandler"))
    addGestureRecognizer(rec)
    calculationContext.onRemove {
        // Retain the sleeve until disposed
        rec.enabled
        actionHolder.description
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NView = UIView

@OptIn(ExperimentalNativeApi::class)
class NViewCalculationContext() : CalculationContext.WithLoadTracking(), Cancellable {

    val onRemove = ArrayList<() -> Unit>()
    override fun cancel() {
        onRemove.forEach { it() }
        onRemove.clear()
    }

    override fun onRemove(action: () -> Unit) {
        onRemove.add(action)
    }

    val loading = Property(false)
    override fun hideLoad() {
        loading.value = false
    }

    override fun showLoad() {
        loading.value = true
    }
}

fun UIView.shutdown() {
    UIViewCalcContext.getValue(this)?.cancel()
    ExtensionProperty.remove(this)
    subviews.forEach { (it as UIView).shutdown() }
}

val UIView.iosCalculationContext: NViewCalculationContext
    get() = UIViewCalcContext.getValue(this) ?: run {
        val new = NViewCalculationContext()
        UIViewCalcContext.setValue(this, new)
        new
    }
actual val NView.calculationContext: CalculationContext
    get() = iosCalculationContext

actual var NView.exists: Boolean
    get() = !hidden
    set(value) {
        if (animationsEnabled) UIView.animateWithDuration(0.15) {
            hidden = !value
            informParentOfSizeChange()
            layoutIfNeeded()
        } else {
            hidden = !value
            informParentOfSizeChange()
        }
    }

actual var NView.visible: Boolean
    get() = throw NotImplementedError()
    set(value) {
        animateIfAllowed {
            alpha = if (value) 1.0 else 0.0
        }
    }

actual var NView.opacity: Double
    get() = throw NotImplementedError()
    set(value) {
        animateIfAllowed {
            alpha = value
        }
    }

@OptIn(ExperimentalForeignApi::class)
actual var NView.nativeRotation: Angle
    get() = throw NotImplementedError()
    set(value) {
        val rotation = CGAffineTransformRotate(this.transform, value.radians.toDouble())
        transform = rotation
    }

actual fun NView.clearNViews() {
    this.subviews.toList().forEach {
        (it as UIView).let {
            it.removeFromSuperview()
            it.shutdown()
        }
    }
}

actual fun NView.addNView(child: NView) {
//    child.setTranslatesAutoresizingMaskIntoConstraints(false)
    this.addSubview(child)
}

actual typealias NContext = UIViewController
actual val NView.nContext: NContext get() {
    return nextResponder?.let {
        if(it is UIViewController) it
        else if(it is UIView) it.nContext
        else throw IllegalStateException()
    } ?: throw IllegalStateException()
}