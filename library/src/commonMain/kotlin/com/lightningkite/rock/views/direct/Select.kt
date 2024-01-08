package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NSelect : NView

@JvmInline
value class Select(override val native: NSelect) : RView<NSelect>

@ViewDsl
expect fun ViewWriter.select(setup: Select.() -> Unit = {}): Unit
expect fun <T> Select.bind(edits: Writable<T>, data: Readable<List<T>>, render: (T)->String)