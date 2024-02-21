package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NSpace : NView

@JvmInline
value class Space(override val native: NSpace) : RView<NSpace>

@ViewDsl
expect fun ViewWriter.spaceActual(setup: Space.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.space(noinline setup: Space.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; spaceActual(setup) }
expect fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit = {})