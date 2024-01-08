package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NSpace : NView

@JvmInline
value class Space(override val native: NSpace) : RView<NSpace>

@ViewDsl
expect fun ViewWriter.space(setup: Space.() -> Unit = {}): Unit
expect fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit = {})