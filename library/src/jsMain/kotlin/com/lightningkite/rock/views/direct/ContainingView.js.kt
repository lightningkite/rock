package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.DynamicCSS
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = HTMLElement

@ViewDsl
actual fun ViewWriter.stackActual(setup: ContainingView.() -> Unit): Unit =
    themedElementBackIfChanged<HTMLDivElement>("div") {
        classList.add("rock-stack")
        setup(ContainingView(this))
    }

@ViewDsl
actual fun ViewWriter.colActual(setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("rock-col")
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.rowActual(setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("rock-row")
    setup(ContainingView(this))
}

actual var ContainingView.spacingMultiplier: Float
    get() {
        return native.style.getPropertyValue("--spacingMultiplier").toFloatOrNull() ?: 1f
    }
    set(value) {
        native.style.setProperty("--spacingMultiplier", value.toString())
        val cn = "spacingMultiplierOf${value.toString().replace(".", "_")}"
        DynamicCSS.styleIfMissing(".$cn > *", mapOf(
            "--parentSpacingMultiplier" to value.toString()
        ))
        native.className = native.className.split(' ').filter { !it.startsWith("spacingMultiplierOf") }.plus(cn).joinToString(" ")
    }