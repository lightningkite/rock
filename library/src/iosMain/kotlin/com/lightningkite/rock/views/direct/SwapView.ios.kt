package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.*
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = FrameLayout

@ViewDsl
actual inline fun ViewWriter.swapViewActual(crossinline setup: SwapView.() -> Unit) = element(FrameLayout()) {
    extensionViewWriter = this@swapViewActual.newViews().also {
        it.includePaddingAtStackEmpty = true
    }
    handleTheme(this, viewDraws = false)
    setup(SwapView(this))
}

@ViewDsl
actual inline fun ViewWriter.swapViewDialogActual(crossinline setup: SwapView.() -> Unit): Unit = element(FrameLayout()) {
    extensionViewWriter = this@swapViewDialogActual.newViews().also {
        it.includePaddingAtStackEmpty = true
    }
    handleTheme(this, viewDraws = false)
    hidden = true
    setup(SwapView(this))
}

actual fun SwapView.swap(transition: ScreenTransition, createNewView: ViewWriter.() -> Unit): Unit {
    native.extensionViewWriter!!.rootCreated = null
    native.withoutAnimation {
        createNewView(native.extensionViewWriter!!.also { it.includePaddingAtStackEmpty = true })
    }
    println("Clearing children...")
    native.clearNViews()
    native.extensionViewWriter!!.rootCreated?.let {
        println("Adding new view...")
        native.addNView(it)
        native.hidden = false
        native.informParentOfSizeChange()
    } ?: run {
        println("Hiding...")
        native.hidden = true
        native.informParentOfSizeChange()
    }
}