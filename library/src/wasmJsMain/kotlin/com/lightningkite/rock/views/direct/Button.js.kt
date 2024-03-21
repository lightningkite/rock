package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchGlobal
import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.calculationContext
import org.w3c.dom.HTMLButtonElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NButton(override val js: HTMLButtonElement): NView2<HTMLButtonElement>()

@ViewDsl
actual inline fun ViewWriter.buttonActual(crossinline setup: Button.() -> Unit): Unit =
    themedElementClickable("button", ::NButton) {
        js.classList.add("rock-stack")
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