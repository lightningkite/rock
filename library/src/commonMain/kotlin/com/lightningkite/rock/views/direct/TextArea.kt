package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NTextArea : NView

@JvmInline
value class TextArea(override val native: NTextArea) : RView<NTextArea>

@ViewDsl
expect fun ViewWriter.textArea(setup: TextArea.() -> Unit = {}): Unit
expect val TextArea.content: Writable<String>
expect var TextArea.keyboardHints: KeyboardHints
expect var TextArea.hint: String