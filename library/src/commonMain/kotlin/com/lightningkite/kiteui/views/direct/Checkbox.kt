package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NCheckbox : NView

@JvmInline
value class Checkbox(override val native: NCheckbox) : RView<NCheckbox>

@ViewDsl
expect fun ViewWriter.checkboxActual(setup: Checkbox.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.checkbox(noinline setup: Checkbox.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; checkboxActual(setup) }
expect var Checkbox.enabled: Boolean
expect val Checkbox.checked: Writable<Boolean>