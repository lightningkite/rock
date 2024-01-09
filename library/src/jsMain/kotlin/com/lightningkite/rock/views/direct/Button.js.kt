package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchGlobal
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
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
    native.onclick = { launchGlobal(action) }
}

actual inline var Button.enabled: Boolean
    get() = !native.disabled
    set(value) {
        native.disabled = !value
    }