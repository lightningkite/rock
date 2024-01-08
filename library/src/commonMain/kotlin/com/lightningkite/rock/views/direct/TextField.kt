package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Action
import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NTextField : NView

@JvmInline
value class TextField(override val native: NTextField) : RView<NTextField>

@ViewDsl
expect fun ViewWriter.textField(setup: TextField.() -> Unit = {}): Unit
expect val TextField.content: Writable<String>
expect var TextField.keyboardHints: KeyboardHints
expect var TextField.action: Action?
expect var TextField.hint: String
expect var TextField.range: ClosedRange<Double>?