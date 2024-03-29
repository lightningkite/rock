package com.lightningkite.rock.views

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.children
import com.lightningkite.rock.models.Align

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContext = Context
actual val NView.nContext: NContext get() = context
actual fun NView.removeNView(child: NView) {
    (this as ViewGroup).removeView(child)
    child.shutdown()
}

actual fun NView.listNViews(): List<NView> = (this as? ViewGroup)?.children?.toList() ?: listOf()

var animationsEnabled: Boolean = true
actual inline fun NView.withoutAnimation(action: () -> Unit) {
    if(!animationsEnabled) {
        action()
        return
    }
    try {
        animationsEnabled = false
        action()
    } finally {
        animationsEnabled = true
    }
}

actual fun NView.scrollIntoView(
    horizontal: Align?,
    vertical: Align?,
    animate: Boolean
) {
}

actual fun NView.consumeInputEvents() {
}