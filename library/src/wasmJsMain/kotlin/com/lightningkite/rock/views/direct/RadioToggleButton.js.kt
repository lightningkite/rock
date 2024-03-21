package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.events.KeyboardEvent

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NRadioToggleButton(override val js: HTMLSpanElement): NView2<HTMLSpanElement>()

@ViewDsl
actual inline fun ViewWriter.radioToggleButtonActual(crossinline setup: RadioToggleButton.() -> Unit): Unit =
    element<HTMLLabelElement>("label") {
        js.classList.add("toggle-button")
        lateinit var input: HTMLInputElement
        element<HTMLInputElement>("input") {
            input = this.js
            this.js.type = "radio"
            js.classList.add("checkResponsive")
            this.js.hidden = true
            this.js.style.display = "none"
        }
        themedElementClickable("span", ::NRadioToggleButton) {
            js.classList.add("checkResponsive", "rock-stack")
            js.tabIndex = 0
            setup(RadioToggleButton(this))
            js.addEventListener("keydown", { ev ->
                ev as KeyboardEvent
                if (ev.key == KeyCodes.space || ev.key == KeyCodes.enter) {
                    ev.preventDefault()
                }
            })
            js.addEventListener("keyup", { ev ->
                ev as KeyboardEvent
                if (ev.key == KeyCodes.space || ev.key == KeyCodes.enter) {
                    input.click()
                    ev.preventDefault()
                }
            })
        }
    }

actual inline var RadioToggleButton.enabled: Boolean
    get() = !(this.native.js.previousElementSibling as HTMLInputElement).disabled
    set(value) {
        (this.native.js.previousElementSibling as HTMLInputElement).disabled = !value
    }
actual val RadioToggleButton.checked: Writable<Boolean>
    get() {
        return (this.native.js.previousElementSibling as HTMLInputElement).vprop(
            "input",
            { checked },
            { checked = it })
    }