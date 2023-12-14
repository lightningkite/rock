package com.lightningkite.rock.views

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.reactive.CalculationContext
import java.lang.RuntimeException

/**
 * A native view in the underlying view system.
 */
actual typealias NView = View

object AndroidAppContext {
    lateinit var applicationCtx: Context
    val res: Resources by lazy { applicationCtx.resources }
    val density: Float by lazy { res.displayMetrics.density }
    val oneRem: Float by lazy { density * 16 }
}

private val View.removeListeners: HashMap<Int, () -> Unit>
    get() = HashMap()

data class NViewCalculationContext(val native: View): CalculationContext {
    override fun onRemove(action: () -> Unit) {
        native.removeListeners[action.hashCode()] = action
    }

    override fun notifyStart() { TODO("NOT YET IMPLEMENTED") }
    override fun notifySuccess() { TODO("NOT YET IMPLEMENTED") }
}

actual val NView.calculationContext: CalculationContext
    get() = NViewCalculationContext(this)

actual var NView.nativeRotation: Angle
    get() = Angle(rotation / Angle.DEGREES_PER_CIRCLE)
    set(value) {
        rotation = value.degrees
    }

actual var NView.opacity: Double
    get() {
        return this.alpha.toDouble()
    }

    set(value) {
        this.alpha = value.toFloat()
    }

actual var NView.exists: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

actual var NView.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

actual fun NView.clearChildren() {
    if (this !is ViewGroup) throw RuntimeException("clearChildren can only be called on Android ViewGroups")
    (this as ViewGroup).removeAllViews()
}

actual fun NView.addChild(child: NView) {
    if (this !is ViewGroup) throw RuntimeException("addChild can only be called on Android ViewGroups")
    (this as ViewGroup).addView(child)
}