package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLSpanElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = HTMLSpanElement

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit): Unit =
    element<HTMLLabelElement>("label") {
        classList.add("toggle-button")
        element<HTMLInputElement>("input") {
            this.type = "radio"
            this.hidden = true
        }
        themedElementClickable<HTMLSpanElement>("span") {
            classList.add("checkResponsive")
            setup(RadioToggleButton(this))
        }
    }

actual inline var RadioToggleButton.enabled: Boolean
    get() = !(this.native.previousElementSibling as HTMLInputElement).disabled
    set(value) {
        (this.native.previousElementSibling as HTMLInputElement).disabled = !value
    }
actual val RadioToggleButton.checked: Writable<Boolean>
    get() {
        return (this.native.previousElementSibling as HTMLInputElement).vprop(
            "input",
            { checked },
            { checked = it })
    }