package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NLabel(override val js: HTMLElement): NView2<HTMLElement>()

@ViewDsl
actual inline fun ViewWriter.labelActual(crossinline setup: Label.() -> Unit): Unit = themedElementBackIfChanged("div", ::NLabel) {
    js.classList.add("kiteui-label")
    textElement("span") {
    }
    setup(Label(this))
}

actual inline var Label.content: String
    get() = (native.js.firstElementChild as? HTMLElement)?.innerText ?: ""
    set(value) {
        (native.js.firstElementChild as? HTMLElement)?.innerText = value
    }