package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import kotlin.jvm.JvmInline
import kotlin.contracts.*


//expect val LocalDateField.content: Writable<LocalDate?>

expect class NRecyclerView : NView

@JvmInline
value class RecyclerView(override val native: NRecyclerView) : RView<NRecyclerView>
@ViewDsl expect fun ViewWriter.recyclerViewActual(setup: RecyclerView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.recyclerView(noinline setup: RecyclerView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; recyclerViewActual(setup) }
@ViewDsl expect fun ViewWriter.horizontalRecyclerViewActual(setup: RecyclerView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.horizontalRecyclerView(noinline setup: RecyclerView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; horizontalRecyclerViewActual(setup) }
expect var RecyclerView.columns: Int
expect fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>)->Unit): Unit
expect fun RecyclerView.scrollToIndex(index: Int, align: Align? = null, animate: Boolean = true)
expect val RecyclerView.firstVisibleIndex: Readable<Int>
expect val RecyclerView.lastVisibleIndex: Readable<Int>