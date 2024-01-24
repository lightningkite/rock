package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.calculationContext
import com.lightningkite.rock.views.launch
import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = HTMLDivElement

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit): Unit =
    themedElementPrivateMeta<NDismissBackground>(
        name = "span",
        themeLogic = { _, _, virtualClasses ->
            virtualClasses.add("dismissBackground")
            virtualClasses.add("rock-stack")
        },
        setup = {
            setup(DismissBackground(this))
        }
    )

actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    native.onclick = { native.calculationContext.launchManualCancel(action) }
}