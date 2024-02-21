package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NContainingView : NView

@JvmInline
value class ContainingView(override val native: NContainingView) : RView<NContainingView>

@ViewDsl
expect fun ViewWriter.stackActual(setup: ContainingView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.stack(noinline setup: ContainingView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; stackActual(setup) }
@ViewDsl
expect fun ViewWriter.colActual(setup: ContainingView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.col(noinline setup: ContainingView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; colActual(setup) }
@ViewDsl
expect fun ViewWriter.rowActual(setup: ContainingView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.row(noinline setup: ContainingView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; rowActual(setup) }