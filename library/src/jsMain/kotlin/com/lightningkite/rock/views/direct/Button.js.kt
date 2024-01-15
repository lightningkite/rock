package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchGlobal
import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.calculationContext
import org.w3c.dom.HTMLButtonElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = HTMLButtonElement

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit): Unit =
    themedElementClickable<NButton>("button") {
        classList.add("rock-stack")
        setup(Button(this))
    }

actual fun Button.onClick(action: suspend () -> Unit): Unit {
    var virtualDisable: Boolean = false
    native.onclick = {
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
    get() = !native.disabled
    set(value) {
        native.disabled = !value
    }