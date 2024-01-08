package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NSwitch : NView

@JvmInline
value class Switch(override val native: NSwitch) : RView<NSwitch>

@ViewDsl
expect fun ViewWriter.switch(setup: Switch.() -> Unit = {}): Unit
expect var Switch.enabled: Boolean
expect val Switch.checked: Writable<Boolean>