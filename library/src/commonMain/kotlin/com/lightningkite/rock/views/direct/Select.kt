package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NSelect : NView

@JvmInline
value class Select(override val native: NSelect) : RView<NSelect>

@ViewDsl
expect fun ViewWriter.selectActual(setup: Select.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.select(noinline setup: Select.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; selectActual(setup) }
expect fun <T> Select.bind(edits: Writable<T>, data: Readable<List<T>>, render: (T)->String)