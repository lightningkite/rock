package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLabel = HTMLElement

@ViewDsl
actual fun ViewWriter.labelActual(setup: Label.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("rock-label")
    textElement("span") {
    }
    setup(Label(this))
}

actual inline var Label.content: String
    get() = (native.firstElementChild as? HTMLElement)?.innerText ?: ""
    set(value) {
        (native.firstElementChild as? HTMLElement)?.innerText = value
    }