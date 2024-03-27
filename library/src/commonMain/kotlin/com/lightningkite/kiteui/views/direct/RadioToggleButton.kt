package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NRadioToggleButton : NView

@JvmInline
value class RadioToggleButton(override val native: NRadioToggleButton) : RView<NRadioToggleButton>

@ViewDsl
expect fun ViewWriter.radioToggleButtonActual(setup: RadioToggleButton.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.radioToggleButton(noinline setup: RadioToggleButton.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; radioToggleButtonActual(setup) }
expect var RadioToggleButton.enabled: Boolean
expect val RadioToggleButton.checked: Writable<Boolean>