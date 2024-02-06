package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.events.KeyboardEvent

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = HTMLSpanElement

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit): Unit =
    element<HTMLLabelElement>("label") {
        classList.add("toggle-button")
        lateinit var input: HTMLInputElement
        element<HTMLInputElement>("input") {
            input = this
            this.type = "radio"
            classList.add("checkResponsive")
            this.hidden = true
            this.style.display = "none"
        }
        themedElementClickable<HTMLSpanElement>("span") {
            classList.add("checkResponsive")
            tabIndex = 0
            setup(RadioToggleButton(this))
            addEventListener("keydown", { ev ->
                ev as KeyboardEvent
                if (ev.key == KeyCodes.space || ev.key == KeyCodes.enter) {
                    ev.preventDefault()
                }
            })
            addEventListener("keyup", { ev ->
                ev as KeyboardEvent
                if (ev.key == KeyCodes.space || ev.key == KeyCodes.enter) {
                    input.click()
                    ev.preventDefault()
                }
            })
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