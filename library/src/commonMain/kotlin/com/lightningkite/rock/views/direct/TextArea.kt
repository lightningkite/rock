package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NTextArea : NView

@JvmInline
value class TextArea(override val native: NTextArea) : RView<NTextArea>

@ViewDsl
expect fun ViewWriter.textAreaActual(setup: TextArea.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.textArea(noinline setup: TextArea.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; textAreaActual(setup) }
expect val TextArea.content: Writable<String>
expect var TextArea.keyboardHints: KeyboardHints
expect var TextArea.hint: String