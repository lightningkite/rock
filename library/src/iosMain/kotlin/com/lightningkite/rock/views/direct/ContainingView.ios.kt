package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.px
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.handleTheme
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = UIView

@ViewDsl
actual inline fun ViewWriter.stackActual(crossinline setup: ContainingView.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual inline fun ViewWriter.colActual(crossinline setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = false
    handleTheme(this, viewDraws = false) {
        gap = (spacingOverride.value ?: it.spacing).value
    }
    setup(ContainingView(this))
}

@ViewDsl
actual inline fun ViewWriter.rowActual(crossinline setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = true
    handleTheme(this, viewDraws = false) {
        gap = (spacingOverride.value ?: it.spacing).value
    }
    setup(ContainingView(this))
}

actual var ContainingView.spacing: Dimension
    get() = when(native) {
        is LinearLayout -> native.spacingOverride.value ?: 0.px
        is FrameLayout -> native.spacingOverride.value ?: 0.px
        else -> 0.px
    }
    set(value) {
        when(native) {
            is LinearLayout -> native.spacingOverride.value = value
            is FrameLayout -> native.spacingOverride.value = value
            else -> {}
        }
    }
