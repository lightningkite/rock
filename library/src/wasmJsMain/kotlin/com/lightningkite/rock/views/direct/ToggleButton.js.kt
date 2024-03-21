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
actual class NToggleButton(override val js: HTMLSpanElement): NView2<HTMLSpanElement>()

@ViewDsl
actual inline fun ViewWriter.toggleButtonActual(crossinline setup: ToggleButton.() -> Unit): Unit = element<HTMLLabelElement>("label") {
    js.classList.add("toggle-button")
    lateinit var input: HTMLInputElement
    element<HTMLInputElement>("input") {
        input = this.js
        this.js.type = "checkbox"
        js.classList.add("checkResponsive")
        this.js.hidden = true
        this.js.style.display = "none"
    }
    themedElementClickable("span", ::NToggleButton) {
        js.classList.add("checkResponsive", "rock-stack")
        js.tabIndex = 0
        setup(ToggleButton(this))
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

actual inline var ToggleButton.enabled: Boolean
    get() = !(this.native.js.previousElementSibling as HTMLInputElement).disabled
    set(value) {
        (this.native.js.previousElementSibling as HTMLInputElement).disabled = !value
    }
actual val ToggleButton.checked: Writable<Boolean>
    get() = (this.native.js.previousElementSibling as HTMLInputElement).vprop(
        "input",
        { checked },
        { checked = it })