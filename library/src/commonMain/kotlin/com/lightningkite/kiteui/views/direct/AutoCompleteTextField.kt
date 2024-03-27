package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.Action
import com.lightningkite.kiteui.models.KeyboardHints
import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NAutoCompleteTextField : NView

@JvmInline
value class AutoCompleteTextField(override val native: NAutoCompleteTextField) : RView<NAutoCompleteTextField>

@ViewDsl
expect fun ViewWriter.autoCompleteTextFieldActual(setup: AutoCompleteTextField.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.autoCompleteTextField(noinline setup: AutoCompleteTextField.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; autoCompleteTextFieldActual(setup) }
expect val AutoCompleteTextField.content: Writable<String>
expect var AutoCompleteTextField.keyboardHints: KeyboardHints
expect var AutoCompleteTextField.action: Action?
expect var AutoCompleteTextField.suggestions: List<String>