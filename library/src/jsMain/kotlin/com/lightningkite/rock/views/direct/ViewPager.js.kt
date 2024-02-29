package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.animationsEnabled
import com.lightningkite.rock.views.forEach
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.SMOOTH
import org.w3c.dom.ScrollBehavior
import org.w3c.dom.ScrollToOptions
import kotlin.math.roundToInt

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NViewPager = HTMLDivElement

@ViewDsl
actual inline fun ViewWriter.viewPagerActual(crossinline setup: ViewPager.() -> Unit) {
    themedElement<HTMLDivElement>("div") {
        this.asDynamic().__ROCK__viewWriter = split()
        classList.add("viewPager")
        setup(ViewPager(this))
    }
}

actual val ViewPager.index: Writable<Int> get() {
    return native.vprop("scroll", { (scrollLeft / clientWidth).roundToInt() }, {
        if(animationsEnabled)
            scrollTo(ScrollToOptions(
                left = (clientWidth * it).toDouble(),
                behavior = ScrollBehavior.SMOOTH
            ))
        else
            scrollLeft = (clientWidth * it).toDouble()
    })
}

actual fun <T> ViewPager.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) {
    (native.asDynamic().__ROCK__viewWriter as ViewWriter).forEachUpdating(items, render = render)
}