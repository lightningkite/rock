package com.lightningkite.mppexample

import org.w3c.dom.*
import kotlin.math.max
import kotlin.math.min

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias RecyclerView = HTMLDivElement

private fun Dimension.toInt(): Int = value.replace("px", "").toInt()

external class ResizeObserver(callback: (Array<ResizeObserverEntry>) -> Unit) {
    fun observe(element: HTMLElement)

    //    fun unobserve(element: HTMLElement)
    fun disconnect()
}

external class ResizeObserverEntry {
    val contentRect: DOMRectReadOnly
    val target: HTMLElement
}

// implementation of binary search such that it returns the index of the first element greater than or equal to the value
private fun Array<Int>.binarySearch(value: Int): Int {
    var low = 0
    var high = size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        val midVal = this[mid]

        if (midVal < value)
            low = mid + 1
        else if (midVal > value)
            high = mid - 1
        else
            return mid // key found
    }
    return low // key not found
}

@ViewDsl
actual fun <T> ViewContext.recyclerView(
    data: List<T>,
    render: NView.(T) -> Unit,
    estimatedItemHeight: Dimension,
): Unit {
    val scrollPositionCache: Array<Int> = Array(data.size) { index -> index * estimatedItemHeight.toInt() }
    val lastIndexInCache = Property(-1)
    val scrollEndPosition = Property(0)
    val outerHeight = Property(0)
    val firstVisibleIndex = Property(0)
    val lastVisibleIndex = Property(0)

    val visibleItems = SharedReadable {
        val firstIndex = firstVisibleIndex.current
        val lastIndex = lastVisibleIndex.current
        if (firstIndex > lastIndex) emptyList()
        else data.subList(firstIndex, lastIndex)
    }
    val innerHeight = SharedReadable {
        if (lastIndexInCache.current == 0) estimatedItemHeight.toInt() * data.size
        else
            scrollPositionCache[lastIndexInCache.current] + (data.size - lastIndexInCache.current) * estimatedItemHeight.toInt()
    }
    val topSpacing = SharedReadable {
//        println("recompute top")
        if (outerHeight.current == 0) 0.px
        else {
            val firstIndex = firstVisibleIndex.current
            if (firstIndex == 0) 0.px
            else
                if (firstIndex >= lastIndexInCache.current) {
                    println("HERE")
                    (scrollPositionCache[lastIndexInCache.current] + (lastIndexInCache.current - firstIndex) * estimatedItemHeight.toInt()).px
                } else {
                    println("HERE2, ${firstIndex}")
                    (scrollPositionCache[firstIndex]).px
                }
        }
    }
    val bottomSpacing = SharedReadable {
//        println("recompute bottom")
        if (outerHeight.current == 0) 0.px
        else (innerHeight.current - topSpacing.current.toInt() - outerHeight.current).px
    }

    column {
        reactiveScope {
            afterTimeout(1) {
                val firstIndex = max(firstVisibleIndex.current, 0)
                val lastIndex = min(lastVisibleIndex.current, data.lastIndex)
                val startIndex = max(firstIndex, lastIndexInCache.once)
                if (startIndex == lastIndex) return@afterTimeout
                for (i in startIndex..lastIndex) {
                    val child = this@column.children[i + 2 - firstIndex] ?: continue
                    val height = child.getBoundingClientRect().height.toInt()
                    scrollPositionCache[i] = height + (if (i == 0) 0 else scrollPositionCache[i - 1])
                }
                lastIndexInCache set max(lastIndex, lastIndexInCache.once)

                if (scrollEndPosition.current > scrollPositionCache[lastIndex]) {
//                check ahead for items that should be visible but aren't yet
                    val newItemsToShow =
                        (scrollEndPosition.current - scrollPositionCache[lastIndex]) / estimatedItemHeight.toInt() + 1
                    lastVisibleIndex set min(lastIndex + newItemsToShow, data.lastIndex)
                }
            }
        }
        val observer = ResizeObserver {
            val height = it[0].contentRect.height.toInt()
            outerHeight set height
            if (visibleItems.once.isEmpty()) {
                val lastIndex = height / estimatedItemHeight.toInt() + 1
                firstVisibleIndex set 0
                lastVisibleIndex set lastIndex
                scrollEndPosition set height
            }
        }
        observer.observe(this@column)
        onRemove { observer.disconnect() }

        addEventListener("scroll", {
            val scrollBottom = min((it.target as HTMLDivElement).offsetHeight + (it.target as HTMLDivElement).scrollTop.toInt(), innerHeight.once)
            val scrollTop = scrollBottom - outerHeight.once
            val firstIndex = scrollPositionCache.binarySearch(scrollTop)
            val lastIndex = scrollPositionCache.binarySearch(scrollBottom)
            scrollEndPosition set scrollBottom
            firstVisibleIndex set max(firstIndex - 1, 0)
            lastVisibleIndex set min(lastIndex + 1, data.lastIndex)
        }, js("{passive:true}"))

        space { ::size { SizeConstraints(minHeight = topSpacing.current, maxHeight = topSpacing.current) } }
        forEach(
            data = { visibleItems.current },
            render = render,
        )
        space { ::size { SizeConstraints(minHeight = bottomSpacing.current, maxHeight = bottomSpacing.current) } }
    } in scrolls()
}
