package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NDismissBackground : NView

@JvmInline
value class DismissBackground(override val native: NDismissBackground) : RView<NDismissBackground>

@ViewDsl
expect fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit = {}): Unit
expect fun DismissBackground.onClick(action: suspend () -> Unit)