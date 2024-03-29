package com.lightningkite.rock.views.direct

import com.lightningkite.rock.contains
import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.views.*
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NDismissBackground(override val js: HTMLElement): NView2<HTMLElement>()

@ViewDsl
actual inline fun ViewWriter.dismissBackgroundActual(crossinline setup: DismissBackground.() -> Unit): Unit {
    stack {
        native.js.classList.add("dismissBackground")
        native.js.onclick = { navigator.dismiss() }
        setup(DismissBackground(NDismissBackground(native.js)))
        native.listNViews().forEach { it.js.onclick = { ev -> ev.stopImmediatePropagation() } }
    }
}

actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    native.js.onclick = { native.calculationContext.launchManualCancel(action) }
}