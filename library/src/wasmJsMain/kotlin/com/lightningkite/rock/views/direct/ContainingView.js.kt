package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.DynamicCSS
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NContainingView(override val js: HTMLElement): NView2<HTMLElement>()

@ViewDsl
actual inline fun ViewWriter.stackActual(crossinline setup: ContainingView.() -> Unit): Unit =
    themedElementBackIfChanged("div", ::NContainingView) {
        js.classList.add("rock-stack")
        setup(ContainingView(this))
    }

@ViewDsl
actual inline fun ViewWriter.colActual(crossinline setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged("div", ::NContainingView) {
    js.classList.add("rock-col")
    setup(ContainingView(this))
}

@ViewDsl
actual inline fun ViewWriter.rowActual(crossinline setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged("div", ::NContainingView) {
    js.classList.add("rock-row")
    setup(ContainingView(this))
}

//actual var ContainingView.spacing: Dimension
//    get() {
//        return Dimension(native.js.style.getPropertyValue("--spacing").takeUnless { it.isBlank() } ?: "0px")
//    }
//    set(value) {
//        native.js.style.setProperty("--spacing", value.value)
//        val cn = "spacingOf${value.value.replace(".", "_").filter { it.isLetterOrDigit() || it == '_' }}"
//        DynamicCSS.styleIfMissing(".$cn > *, .$cn > .hidingContainer > *", mapOf(
//            "--parentSpacing" to value.value
//        ))
//        native.js.className = native.js.className.split(' ').filter { !it.startsWith("spacingOf") }.plus(cn).joinToString(" ")
//    }