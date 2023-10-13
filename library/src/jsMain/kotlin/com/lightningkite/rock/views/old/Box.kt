package com.lightningkite.rock.views.old

import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Box = HTMLDivElement

@ViewDsl
actual inline fun ViewContext.box(setup: Box.() -> Unit): Unit = element<HTMLDivElement>("div") {
    setup()
}
