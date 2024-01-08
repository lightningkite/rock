package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NLabel : NView

@JvmInline
value class Label(override val native: NLabel) : RView<NLabel>

@ViewDsl
expect fun ViewWriter.label(setup: Label.() -> Unit = {}): Unit
expect var Label.content: String