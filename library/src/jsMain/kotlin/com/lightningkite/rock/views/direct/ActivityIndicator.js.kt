package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.dom.addClass
import org.w3c.dom.HTMLSpanElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NActivityIndicator = HTMLSpanElement

@ViewDsl
actual inline fun ViewWriter.activityIndicatorActual(crossinline setup: ActivityIndicator.() -> Unit): Unit =
    themedElement<HTMLSpanElement>("span") {
        addClass("spinner")
    }