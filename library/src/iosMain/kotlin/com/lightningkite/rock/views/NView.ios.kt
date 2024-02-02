package com.lightningkite.rock.views

import com.lightningkite.rock.models.Align
import platform.UIKit.*

actual fun NView.removeNView(child: NView) {
    child.removeFromSuperview()
    child.shutdown()
}

actual fun NView.listNViews(): List<NView> {
    return subviews.map { it as UIView }
}

actual inline fun NView.withoutAnimation(action: () -> Unit) {
}

actual fun NView.scrollIntoView(
    horizontal: Align?,
    vertical: Align?,
    animate: Boolean
) {
}

actual fun NView.consumeInputEvents() {
}