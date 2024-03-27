package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NLabel : NView

@JvmInline
value class Label(override val native: NLabel) : RView<NLabel>

@ViewDsl
expect fun ViewWriter.labelActual(setup: Label.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.label(noinline setup: Label.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; labelActual(setup) }
expect var Label.content: String