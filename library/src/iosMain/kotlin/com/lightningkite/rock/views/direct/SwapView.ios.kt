package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.*
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = FrameLayout

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit) = element(FrameLayout()) {
    extensionViewWriter = this@swapView.split()
    handleTheme(this, viewDraws = false)
    setup(SwapView(this))
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit): Unit = element(FrameLayout()) {
    extensionViewWriter = this@swapViewDialog.split()
    handleTheme(this, viewDraws = false)
    hidden = true
    setup(SwapView(this))
}

actual fun SwapView.swap(transition: ScreenTransition, createNewView: ViewWriter.() -> Unit): Unit {
    native.withoutAnimation {
        native.clearNViews()
        createNewView(native.extensionViewWriter!!)
        native.hidden = native.subviews.all { (it as UIView).hidden }
    }
}