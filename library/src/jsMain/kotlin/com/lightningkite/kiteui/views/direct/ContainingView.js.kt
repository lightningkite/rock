package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.Dimension
import com.lightningkite.kiteui.views.DynamicCSS
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = HTMLElement

@ViewDsl
actual inline fun ViewWriter.stackActual(crossinline setup: ContainingView.() -> Unit): Unit =
    themedElementBackIfChanged<HTMLDivElement>("div") {
        classList.add("kiteui-stack")
        setup(ContainingView(this))
    }

@ViewDsl
actual inline fun ViewWriter.colActual(crossinline setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("kiteui-col")
    setup(ContainingView(this))
}

@ViewDsl
actual inline fun ViewWriter.rowActual(crossinline setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("kiteui-row")
    setup(ContainingView(this))
}

//actual var ContainingView.spacing: Dimension
//    get() {
//        return Dimension(native.style.getPropertyValue("--spacing").takeUnless { it.isBlank() } ?: "0px")
//    }
//    set(value) {
//        native.style.setProperty("--spacing", value.value)
//        val cn = "spacingOf${value.value.replace(".", "_").filter { it.isLetterOrDigit() || it == '_' }}"
//        DynamicCSS.styleIfMissing(".$cn > *, .$cn > .hidingContainer > *", mapOf(
//            "--parentSpacing" to value.value
//        ))
//        native.className = native.className.split(' ').filter { !it.startsWith("spacingOf") }.plus(cn).joinToString(" ")
//    }