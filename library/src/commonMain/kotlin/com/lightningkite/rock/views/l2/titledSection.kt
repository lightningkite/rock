package com.lightningkite.rock.views.l2

import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.viewWriterAddon

var ViewWriter.titleDepth: Int by viewWriterAddon(0)

@ViewDsl
fun ViewWriter.titledSection(
    title: String,
    content: () -> Unit,
) = titledSection({ this.content = title }, content)

@ViewDsl
fun ViewWriter.titledSection(
    titleSetup: TextView.() -> Unit = {},
    content: () -> Unit,
) {
    col {
        try {
            when (++titleDepth) {
                1 -> h1 { titleSetup() }
                2 -> h2 { titleSetup() }
                3 -> h3 { titleSetup() }
                4 -> h4 { titleSetup() }
                5 -> h5 { titleSetup() }
                else -> h6 { titleSetup() }
            }
            content()
        } finally {
            titleDepth--
        }
        space(4.0)
    }
}
