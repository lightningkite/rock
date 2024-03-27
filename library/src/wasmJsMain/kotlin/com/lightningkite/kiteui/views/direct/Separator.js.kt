package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NSeparator(override val js: HTMLElement): NView2<HTMLElement>()

@ViewDsl
actual inline fun ViewWriter.separatorActual(crossinline setup: Separator.() -> Unit): Unit = themedElement("div", ::NSeparator) {
    js.classList.add("kiteui-separator")
    setup(Separator(this))
}