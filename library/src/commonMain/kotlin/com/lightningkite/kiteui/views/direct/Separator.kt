package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NSeparator : NView
@JvmInline
value class Separator(override val native: NSeparator) : RView<NSeparator>
@ViewDsl
expect fun ViewWriter.separatorActual(setup: Separator.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.separator(noinline setup: Separator.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; separatorActual(setup) }