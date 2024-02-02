package com.lightningkite.rock.views

import com.lightningkite.rock.models.Align

actual class NContext { companion object { val shared = NContext() }}
actual val NView.nContext: NContext get() = NContext.shared
actual fun NView.removeNView(child: NView) {
}

actual fun NView.listNViews(): List<NView> = listOf()
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