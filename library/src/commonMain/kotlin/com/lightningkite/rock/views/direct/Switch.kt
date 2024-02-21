package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NSwitch : NView

@JvmInline
value class Switch(override val native: NSwitch) : RView<NSwitch>

@ViewDsl
expect fun ViewWriter.switchActual(setup: Switch.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.switch(noinline setup: Switch.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; switchActual(setup) }
expect var Switch.enabled: Boolean
expect val Switch.checked: Writable<Boolean>