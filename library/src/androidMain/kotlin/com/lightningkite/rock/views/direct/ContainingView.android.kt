package com.lightningkite.rock.views.direct

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.lightningkite.rock.models.LinearGradient
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = ViewGroup

@ViewDsl
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit) = viewElement(
    factory = ::FrameLayout,
    wrapper = ::ContainingView
) {
    handleTheme(native, viewDraws = false)
    setup(this)
}

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit) {
    viewElement(factory = ::LinearLayout, wrapper = ::ContainingView) {
        val l = native as LinearLayout
        l.orientation = LinearLayout.VERTICAL
        l.gravity = Gravity.CENTER_HORIZONTAL
        handleTheme(l, viewDraws = false)
        setup(ContainingView(l))
    }
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit) {
    viewElement(factory = ::LinearLayout, wrapper = ::ContainingView) {
        val l = native as LinearLayout
        l.orientation = LinearLayout.HORIZONTAL
        l.gravity = Gravity.CENTER_VERTICAL
        handleTheme(l, viewDraws = false)
        setup(ContainingView(l))
    }
}

private fun LinearGradient.orientation(): GradientDrawable.Orientation {
    return when (angle.degrees.toInt()) {
        in 0..90 -> GradientDrawable.Orientation.LEFT_RIGHT
        in 91..180 -> GradientDrawable.Orientation.TOP_BOTTOM
        in 181..270 -> GradientDrawable.Orientation.RIGHT_LEFT
        in 271..360 -> GradientDrawable.Orientation.BOTTOM_TOP
        else -> GradientDrawable.Orientation.LEFT_RIGHT
    }
}