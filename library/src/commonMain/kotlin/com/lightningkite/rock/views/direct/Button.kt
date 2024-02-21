package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NButton : NView

@JvmInline
value class Button(override val native: NButton) : RView<NButton>

@ViewDsl
expect fun ViewWriter.buttonActual(setup: Button.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.button(noinline setup: Button.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; buttonActual(setup) }
expect fun Button.onClick(action: suspend () -> Unit)
expect var Button.enabled: Boolean