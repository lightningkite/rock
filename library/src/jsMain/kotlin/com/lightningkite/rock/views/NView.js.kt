package com.lightningkite.rock.views

import org.w3c.dom.HTMLElement
import org.w3c.dom.get

actual class NContext { companion object { val shared = NContext() }}
actual val NView.nContext: NContext get() = NContext.shared
actual fun NView.removeNView(child: NView) {
    this.removeChild(child)
}

actual fun NView.listNViews(): List<NView> = children.let {
    (0..<it.length).mapNotNull { index -> it.get(index) as? HTMLElement }.toList()
}