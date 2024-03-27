package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.launchGlobal
import com.lightningkite.kiteui.launchManualCancel
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.calculationContext
import org.w3c.dom.HTMLButtonElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NButton(override val js: HTMLButtonElement): NView2<HTMLButtonElement>()

@ViewDsl
actual inline fun ViewWriter.buttonActual(crossinline setup: Button.() -> Unit): Unit =
    themedElementClickable("button", ::NButton) {
        js.classList.add("kiteui-stack")
        js.scrollLeft
        setup(Button(this))
    }

actual fun Button.onClick(action: suspend () -> Unit): Unit {
    var virtualDisable: Boolean = false
    native.js.onclick = {
        if(!virtualDisable) {
            native.calculationContext.launchManualCancel {
                try {
                    virtualDisable = true
                    action()
                } finally {
                    virtualDisable = false
                }
            }
        }
    }
}

actual inline var Button.enabled: Boolean
    get() = !native.js.disabled
    set(value) {
        native.js.disabled = !value
    }