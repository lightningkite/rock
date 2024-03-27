package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.contracts.*
import kotlin.jvm.JvmInline

expect class NRadioButton : NView

@JvmInline
value class RadioButton(override val native: NRadioButton) : RView<NRadioButton>

@ViewDsl
expect fun ViewWriter.radioButtonActual(setup: RadioButton.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.radioButton(noinline setup: RadioButton.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; radioButtonActual(setup) }
expect var RadioButton.enabled: Boolean
expect val RadioButton.checked: Writable<Boolean>