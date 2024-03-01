package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.docs.code
import com.lightningkite.mppexampleapp.docs.example
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

actual fun ViewWriter.platformSpecific() {
    col {
        card - row {
            expanding - scrollsHorizontally - code - text(
                """
                    Example Text
                    """.trimIndent()
            )
            separator()
            expanding - card - stack {
                text("Hello world")
            }
        }
        card - row {
            expanding - scrollsHorizontally - code - text(
                """
                    Example Text
                    """.trimIndent()
            )
            separator()
            expanding - card - stack {
                text("Hello world")
            }
        }
    }
}