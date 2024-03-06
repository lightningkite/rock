package com.lightningkite.rock.views.direct

import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NLink : NView

@JvmInline
value class Link(override val native: NLink) : RView<NLink>

@ViewDsl
expect fun ViewWriter.linkActual(setup: Link.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.link(noinline setup: Link.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; linkActual(setup) }
expect var Link.to: RockScreen
expect var Link.newTab: Boolean
expect fun Link.onNavigate(action: suspend () -> Unit)
