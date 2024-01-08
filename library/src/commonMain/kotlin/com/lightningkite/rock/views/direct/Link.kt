package com.lightningkite.rock.views.direct

import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NLink : NView

@JvmInline
value class Link(override val native: NLink) : RView<NLink>

@ViewDsl
expect fun ViewWriter.link(setup: Link.() -> Unit = {}): Unit
expect var Link.to: RockScreen
expect var Link.newTab: Boolean