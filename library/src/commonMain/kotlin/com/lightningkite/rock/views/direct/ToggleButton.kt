package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NToggleButton : NView

@JvmInline
value class ToggleButton(override val native: NToggleButton) : RView<NToggleButton>

@ViewDsl
expect fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit = {}): Unit
expect var ToggleButton.enabled: Boolean
expect val ToggleButton.checked: Writable<Boolean>