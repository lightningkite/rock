package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.handleTheme
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = UIView

@ViewDsl
actual fun ViewWriter.stackActual(setup: ContainingView.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.colActual(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = false
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.rowActual(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = true
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

actual var ContainingView.spacingMultiplier: Float
    get() = when(native) {
        is LinearLayout -> native.spacingMultiplier.toFloat()
        is FrameLayout -> native.spacingMultiplier.toFloat()
        else -> 1f
    }
    set(value) {
        when(native) {
            is LinearLayout -> native.spacingMultiplier = value.toDouble()
            is FrameLayout -> native.spacingMultiplier = value.toDouble()
            else -> 1f
        }
    }