package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NDismissBackground : NView

@JvmInline
value class DismissBackground(override val native: NDismissBackground) : RView<NDismissBackground>

@ViewDsl
expect fun ViewWriter.dismissBackgroundActual(setup: DismissBackground.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.dismissBackground(noinline setup: DismissBackground.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; dismissBackgroundActual(setup) }
expect fun DismissBackground.onClick(action: suspend () -> Unit)