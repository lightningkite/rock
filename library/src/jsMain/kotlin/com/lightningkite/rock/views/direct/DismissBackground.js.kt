package com.lightningkite.rock.views.direct

import com.lightningkite.rock.contains
import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.calculationContext
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = HTMLElement

@ViewDsl
actual fun ViewWriter.dismissBackgroundActual(setup: DismissBackground.() -> Unit): Unit {
    stack {
        themedElement<HTMLSpanElement>(
            name = "span",
            setup = {
                classList.add("dismissBackground")
            }
        ) 
        setup(DismissBackground(native))
    }
}

actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    (native.children[0] as HTMLElement).onclick = { native.calculationContext.launchManualCancel(action) }
}