package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewWriter

actual class NViewPager : NView()

@ViewDsl
actual fun ViewWriter.viewPager(setup: ViewPager.() -> Unit) {
}

actual val ViewPager.index: Writable<Int>
    get() = TODO("Not yet implemented")

actual fun <T> ViewPager.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) {
}