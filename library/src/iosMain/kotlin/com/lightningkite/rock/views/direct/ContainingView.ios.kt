package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.handleTheme
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = UIView

@ViewDsl
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = false
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = true
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

actual var ContainingView.spacing: Dimension
    get() = TODO("Not yet implemented")
    set(value) {}