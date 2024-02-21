package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Action
import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
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