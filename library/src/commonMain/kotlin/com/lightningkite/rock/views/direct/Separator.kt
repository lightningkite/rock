package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NSeparator : NView
@JvmInline
value class Separator(override val native: NSeparator) : RView<NSeparator>
@ViewDsl
expect fun ViewWriter.separator(setup: Separator.() -> Unit = {}): Unit