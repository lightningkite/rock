package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NToggleButton : NView

@JvmInline
value class ToggleButton(override val native: NToggleButton) : RView<NToggleButton>

@ViewDsl
expect fun ViewWriter.toggleButtonActual(setup: ToggleButton.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.toggleButton(noinline setup: ToggleButton.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; toggleButtonActual(setup) }
expect var ToggleButton.enabled: Boolean
expect val ToggleButton.checked: Writable<Boolean>