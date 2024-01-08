package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NButton : NView

@JvmInline
value class Button(override val native: NButton) : RView<NButton>

@ViewDsl
expect fun ViewWriter.button(setup: Button.() -> Unit = {}): Unit
expect fun Button.onClick(action: suspend () -> Unit)
expect var Button.enabled: Boolean