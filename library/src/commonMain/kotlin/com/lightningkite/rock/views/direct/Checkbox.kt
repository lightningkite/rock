package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NCheckbox : NView

@JvmInline
value class Checkbox(override val native: NCheckbox) : RView<NCheckbox>

@ViewDsl
expect fun ViewWriter.checkbox(setup: Checkbox.() -> Unit = {}): Unit
expect var Checkbox.enabled: Boolean
expect val Checkbox.checked: Writable<Boolean>