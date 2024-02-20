package com.lightningkite.rock.views.direct

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.LinearGradient
import com.lightningkite.rock.models.px
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.lparams

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
    viewElement(factory = ::SlightlyModifiedLinearLayout, wrapper = ::ContainingView) {
        val l = native as LinearLayoutCompat
        l.orientation = LinearLayoutCompat.VERTICAL
        l.gravity = Gravity.CENTER_HORIZONTAL
        l.setLayoutTransition(LayoutTransition())
        handleTheme(l, viewDraws = false)
        setup(ContainingView(l))
    }
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit) {
    viewElement(factory = ::SlightlyModifiedLinearLayout, wrapper = ::ContainingView) {
        val l = native as LinearLayoutCompat
        l.orientation = LinearLayoutCompat.HORIZONTAL
        l.gravity = Gravity.CENTER_VERTICAL
        l.setLayoutTransition(LayoutTransition())
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

class SlightlyModifiedLinearLayout(context: Context): LinearLayoutCompat(context) {
    var overrideSpacing: Dimension? = null
    override fun generateDefaultLayoutParams(): LayoutParams? {
        if (orientation == HORIZONTAL) {
            return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        } else if (orientation == VERTICAL) {
            return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        return null
    }
}

actual var ContainingView.spacing: Dimension
    get() = TODO()
    set(value) {
        (native as SlightlyModifiedLinearLayout).overrideSpacing = value
    }