package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.clearChildren
import com.lightningkite.rock.views.handleTheme
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = FrameLayout

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit) = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    setup(SwapView(this))
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    hidden = true
    setup(SwapView(this))
}

actual fun SwapView.swap(transition: ScreenTransition, createNewView: () -> Unit): Unit {
    native.clearChildren()
    createNewView()
    native.hidden = native.subviews.all { (it as UIView).hidden }
}