package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.times
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.element
import com.lightningkite.kiteui.views.reactiveScope
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NSpace(override val js: HTMLElement): NView2<HTMLElement>()

@ViewDsl
actual inline fun ViewWriter.spaceActual(crossinline setup: Space.() -> Unit): Unit = element("span", ::NSpace) {
    val getter = currentTheme
    val s = Space(this)
    s.reactiveScope {
        js.style.width = (getter().spacing).value
        js.style.height = (getter().spacing).value
    }
    setup(s)
}

actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = element("span", ::NSpace) {
    val getter = currentTheme
    val s = Space(this)
    s.reactiveScope {
        js.style.width = (getter().spacing * multiplier).value
        js.style.height = (getter().spacing * multiplier).value
    }
    setup(s)
}