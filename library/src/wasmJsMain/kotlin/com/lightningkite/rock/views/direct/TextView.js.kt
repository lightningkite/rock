package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NTextView(override val js: HTMLElement): NView2<HTMLElement>()

@ViewDsl
actual inline fun ViewWriter.h1Actual(crossinline setup: TextView.() -> Unit): Unit = headerElement("h1", setup)

@ViewDsl
actual inline fun ViewWriter.h2Actual(crossinline setup: TextView.() -> Unit): Unit = headerElement("h2", setup)

@ViewDsl
actual inline fun ViewWriter.h3Actual(crossinline setup: TextView.() -> Unit): Unit = headerElement("h3", setup)

@ViewDsl
actual inline fun ViewWriter.h4Actual(crossinline setup: TextView.() -> Unit): Unit = headerElement("h4", setup)

@ViewDsl
actual inline fun ViewWriter.h5Actual(crossinline setup: TextView.() -> Unit): Unit = headerElement("h5", setup)

@ViewDsl
actual inline fun ViewWriter.h6Actual(crossinline setup: TextView.() -> Unit): Unit = headerElement("h6", setup)

@ViewDsl
actual inline fun ViewWriter.textActual(crossinline setup: TextView.() -> Unit): Unit = textElement("p", setup)

@ViewDsl
actual inline fun ViewWriter.subtextActual(crossinline setup: TextView.() -> Unit): Unit = textElement("span") {
    native.js.classList.add("subtext")
    setup()
}

actual inline var TextView.content: String
    get() = native.js.innerText
    set(value) {
        native.js.innerText = value
    }
actual inline var TextView.align: Align
    get() = when (window.getComputedStyle(native.js).textAlign) {
        "start" -> Align.Start
        "center" -> Align.Center
        "end" -> Align.End
        "justify" -> Align.Stretch
        else -> Align.Start
    }
    set(value) {
        native.js.style.textAlign = when (value) {
            Align.Start -> "start"
            Align.Center -> "center"
            Align.End -> "end"
            Align.Stretch -> "justify"
        }
    }
actual inline var TextView.textSize: Dimension
    get() = Dimension(window.getComputedStyle(native.js).fontSize)
    set(value) {
        native.js.style.fontSize = value.value
    }