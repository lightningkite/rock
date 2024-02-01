package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLSpanElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = HTMLSpanElement

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit): Unit = element<HTMLLabelElement>("label") {
    classList.add("toggle-button")
    element<HTMLInputElement>("input") {
        this.type = "checkbox"
        classList.add("checkResponsive")
        this.hidden = true
        this.style.display = "none"
    }
    themedElementClickable<HTMLSpanElement>("span") {
        classList.add("checkResponsive")
        setup(ToggleButton(this))
    }
}

actual inline var ToggleButton.enabled: Boolean
    get() = !(this.native.previousElementSibling as HTMLInputElement).disabled
    set(value) {
        (this.native.previousElementSibling as HTMLInputElement).disabled = !value
    }
actual val ToggleButton.checked: Writable<Boolean>
    get() = (this.native.previousElementSibling as HTMLInputElement).vprop(
        "input",
        { checked },
        { checked = it })