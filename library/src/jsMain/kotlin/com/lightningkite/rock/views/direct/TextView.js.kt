package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextView = HTMLElement

@ViewDsl
actual fun ViewWriter.h1Actual(setup: TextView.() -> Unit): Unit = headerElement("h1", setup)

@ViewDsl
actual fun ViewWriter.h2Actual(setup: TextView.() -> Unit): Unit = headerElement("h2", setup)

@ViewDsl
actual fun ViewWriter.h3Actual(setup: TextView.() -> Unit): Unit = headerElement("h3", setup)

@ViewDsl
actual fun ViewWriter.h4Actual(setup: TextView.() -> Unit): Unit = headerElement("h4", setup)

@ViewDsl
actual fun ViewWriter.h5Actual(setup: TextView.() -> Unit): Unit = headerElement("h5", setup)

@ViewDsl
actual fun ViewWriter.h6Actual(setup: TextView.() -> Unit): Unit = headerElement("h6", setup)

@ViewDsl
actual fun ViewWriter.textActual(setup: TextView.() -> Unit): Unit = textElement("p", setup)

@ViewDsl
actual fun ViewWriter.subtextActual(setup: TextView.() -> Unit): Unit = textElement("span") {
    native.classList.add("subtext")
    setup()
}

actual inline var TextView.content: String
    get() = native.innerText
    set(value) {
        native.innerText = value
    }
actual inline var TextView.align: Align
    get() = when (window.getComputedStyle(native).textAlign) {
        "start" -> Align.Start
        "center" -> Align.Center
        "end" -> Align.End
        "justify" -> Align.Stretch
        else -> Align.Start
    }
    set(value) {
        native.style.textAlign = when (value) {
            Align.Start -> "start"
            Align.Center -> "center"
            Align.End -> "end"
            Align.Stretch -> "justify"
        }
    }
actual inline var TextView.textSize: Dimension
    get() = Dimension(window.getComputedStyle(native).fontSize)
    set(value) {
        native.style.fontSize = value.value
    }