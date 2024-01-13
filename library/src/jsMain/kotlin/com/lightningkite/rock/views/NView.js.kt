package com.lightningkite.rock.views

import com.lightningkite.rock.models.Align
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

fun NView.scrollIntoView(horizontal: Align?, vertical: Align?, animate: Boolean) {
    val d: dynamic = js("{}")
    d.behavior = if(animate) "smooth" else "instant"
    d.inline = when(horizontal) {
        Align.Start -> "start"
        Align.Center -> "center"
        Align.End -> "end"
        else -> "nearest"
    }
    d.block = when(vertical) {
        Align.Start -> "start"
        Align.Center -> "center"
        Align.End -> "end"
        else -> "nearest"
    }
    this.scrollIntoView(d)
}