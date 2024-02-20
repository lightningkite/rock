package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = HTMLElement

@ViewDsl
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit): Unit =
    themedElementBackIfChanged<HTMLDivElement>("div") {
        classList.add("rock-stack")
        setup(ContainingView(this))
    }

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("rock-col")
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("rock-row")
    setup(ContainingView(this))
}

actual var ContainingView.spacing: Dimension
    get() = TODO("Not yet implemented")
    set(value) {}