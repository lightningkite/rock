package com.lightningkite.rock.views.direct

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NViewPager = HTMLElement

@ViewDsl
actual fun ViewWriter.viewPagerActual(setup: ViewPager.() -> Unit) {
}

actual val ViewPager.index: Writable<Int>
    get() = TODO("Not yet implemented")

actual fun <T> ViewPager.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) {
}