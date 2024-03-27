package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import org.w3c.dom.HTMLInputElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NSwitch(override val js: HTMLInputElement): NView2<HTMLInputElement>()

@ViewDsl
actual inline fun ViewWriter.switchActual(crossinline setup: Switch.() -> Unit): Unit = themedElementClickable("input", ::NSwitch) {
    js.type = "checkbox"
    js.classList.add("switch")
    js.classList.add("checkResponsive")
    setup(Switch(this))
}

actual inline var Switch.enabled: Boolean
    get() = !native.js.disabled
    set(value) {
        native.js.disabled = !value
    }
actual val Switch.checked: Writable<Boolean> get() = native.js.vprop("input", { checked }, { checked = it })