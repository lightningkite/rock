package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NSpace : NView

@JvmInline
value class Space(override val native: NSpace) : RView<NSpace>

@ViewDsl
expect fun ViewWriter.spaceActual(setup: Space.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.space(noinline setup: Space.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; spaceActual(setup) }
expect fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit = {})