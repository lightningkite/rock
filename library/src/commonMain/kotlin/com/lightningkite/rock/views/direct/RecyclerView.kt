package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import kotlin.jvm.JvmInline


//expect val LocalDateField.content: Writable<LocalDate?>

expect class NRecyclerView : NView

@JvmInline
value class RecyclerView(override val native: NRecyclerView) : RView<NRecyclerView>
@ViewDsl expect fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
expect var RecyclerView.columns: Int
expect fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>)->Unit): Unit
