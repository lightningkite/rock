package com.lightningkite.rock.views.direct

import android.view.View
import android.widget.FrameLayout
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = FrameLayout

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit) {
    return viewElement(factory = ::FrameLayout, wrapper = ::SwapView, setup = setup)
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit) {

}

actual fun SwapView.swap(
    transition: ScreenTransition,
    createNewView: () -> Unit,
) {
    val oldView = this.native.getChildAt(0)
    val newViewIndex = if (oldView == null) {
        0
    } else {
        1
    }

    val newView = this.native.getChildAt(newViewIndex)

    TransitionManager.beginDelayedTransition(native, TransitionSet().apply {
        transition.exit?.addTarget(oldView ?: View(native.context))
        transition.enter?.addTarget(newView)
        transition.enter?.let { addTransition(it) }
        transition.exit?.let { addTransition(it) }
    })
    oldView?.let { oldNN -> native.removeView(oldNN) }
    createNewView()
}