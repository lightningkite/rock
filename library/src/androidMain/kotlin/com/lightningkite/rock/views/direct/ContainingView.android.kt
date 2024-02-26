package com.lightningkite.rock.views.direct

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.LinearGradient
import com.lightningkite.rock.models.px
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = ViewGroup

@ViewDsl
actual fun ViewWriter.stackActual(setup: ContainingView.() -> Unit) = viewElement(
    factory = ::FrameLayout,
    wrapper = ::ContainingView
) {
    handleTheme(native, viewDraws = false)
    setup(this)
}

@ViewDsl
actual fun ViewWriter.colActual(setup: ContainingView.() -> Unit) {
    viewElement(factory = ::SlightlyModifiedLinearLayout, wrapper = ::ContainingView) {
        val l = native as SlightlyModifiedLinearLayout
        l.vertical = true
        l.setLayoutTransition(LayoutTransition())
        handleTheme(l, viewDraws = false) { t, v ->
            v.gap = (v.spacingOverride.value ?: t.spacing).value.toInt()
        }
        setup(ContainingView(l))
    }
}

@ViewDsl
actual fun ViewWriter.rowActual(setup: ContainingView.() -> Unit) {
    viewElement(factory = ::SlightlyModifiedLinearLayout, wrapper = ::ContainingView) {
        val l = native as SlightlyModifiedLinearLayout
        l.vertical = false
        l.setLayoutTransition(LayoutTransition())
        handleTheme(l, viewDraws = false) { t, v ->
            v.gap = (v.spacingOverride.value ?: t.spacing).value.toInt()
        }
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

interface HasSpacingMultiplier {
    val spacingOverride: Property<Dimension?>
}

class SlightlyModifiedFrameLayout(context: Context) : FrameLayout(context), HasSpacingMultiplier {
    override val spacingOverride: Property<Dimension?> = Property<Dimension?>(null)
}

class SlightlyModifiedLinearLayout(context: Context) : SimplifiedLinearLayout(context), HasSpacingMultiplier {
    override val spacingOverride: Property<Dimension?> = Property<Dimension?>(null).apply {
        addListener {
            value?.value?.toInt()?.let {
                this@SlightlyModifiedLinearLayout.gap = it
            }
        }
    }
}

actual var ContainingView.spacing: Dimension
    get() = (native as HasSpacingMultiplier).spacingOverride.value ?: 0.px
    set(value) {
        (native as HasSpacingMultiplier).spacingOverride.value = value
    }