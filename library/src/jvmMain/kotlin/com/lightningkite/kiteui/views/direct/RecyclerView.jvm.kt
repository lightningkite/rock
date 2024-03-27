package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.Align
import com.lightningkite.kiteui.reactive.Readable

actual fun RecyclerView.scrollToIndex(
    index: Int,
    align: Align?,
    animate: Boolean
) {
}

actual val RecyclerView.firstVisibleIndex: Readable<Int>
    get() = TODO("Not yet implemented")
actual val RecyclerView.lastVisibleIndex: Readable<Int>
    get() = TODO("Not yet implemented")