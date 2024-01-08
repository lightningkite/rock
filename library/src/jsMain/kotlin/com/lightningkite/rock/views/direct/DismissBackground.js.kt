package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
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
            virtualClasses.add("inclBack")
        },
        setup = {
            setup(DismissBackground(this))
        }
    )

actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    native.onclick = { launch { action() } }
}