package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSeparator = HTMLElement

@ViewDsl
actual inline fun ViewWriter.separatorActual(crossinline setup: Separator.() -> Unit): Unit = themedElement<HTMLDivElement>("div") {
    classList.add("rock-separator")
    setup(Separator(this))
}