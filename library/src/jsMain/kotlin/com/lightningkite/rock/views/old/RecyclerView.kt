package com.lightningkite.rock.views.old

import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.ReactiveScope
import com.lightningkite.rock.reactive.SharedReadable
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import org.w3c.dom.*
import kotlin.math.max
import kotlin.math.min

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
    data: ReactiveScope.() -> List<T>,
    render: ViewContext.(T) -> Unit,
    estimatedItemHeightInPixels: Int,
) {
    column {
        val context = derive(this)
        reactiveScope {
            innerHTML = ""
            with(context) {
                recyclerView(
                    data = data(),
                    render = render,
                    estimatedItemHeightInPixels = estimatedItemHeightInPixels,
                ) in weight(1f) in scrolls()
            }
        }
    }
}

@ViewDsl
actual fun <T> ViewContext.recyclerView(
    data: List<T>,
    render: ViewContext.(T) -> Unit,
    estimatedItemHeightInPixels: Int,
) {
    val scrollPositionCache: Array<Int> = Array(data.size) { index -> index * estimatedItemHeightInPixels }
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
        if (lastIndexInCache.current == 0) estimatedItemHeightInPixels * data.size
        else
            scrollPositionCache[lastIndexInCache.current] + (data.size - lastIndexInCache.current) * estimatedItemHeightInPixels
    }
    val topSpacing = SharedReadable {
        if (outerHeight.current == 0) 0
        else {
            val firstIndex = firstVisibleIndex.current
            if (firstIndex == 0) 0
            else
                if (firstIndex >= lastIndexInCache.current) {
                    scrollPositionCache[lastIndexInCache.current] + (lastIndexInCache.current - firstIndex) * estimatedItemHeightInPixels
                } else {
                    scrollPositionCache[firstIndex]
                }
        }
    }
    val bottomSpacing = SharedReadable {
        if (outerHeight.current == 0) 0
        else innerHeight.current - topSpacing.current - outerHeight.current
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
                    if (i >= scrollPositionCache.size) break
                    scrollPositionCache[i + 1] = height + scrollPositionCache[i]
                }
                lastIndexInCache set max(lastIndex, lastIndexInCache.once)

//                check ahead for items that should be visible but aren't yet
                if (scrollEndPosition.current > scrollPositionCache[lastIndex]) {
                    val newItemsToShow =
                        (scrollEndPosition.current - scrollPositionCache[lastIndex]) / estimatedItemHeightInPixels + 1
                    lastVisibleIndex set min(lastIndex + newItemsToShow, data.lastIndex)
                }
            }
        }
        val observer = ResizeObserver { it, observer ->
            val height = it[0].contentRect.height.toInt()
            outerHeight set height
            this@column.style.maxHeight = "${height}px"
            if (visibleItems.once.isEmpty()) {
                val lastIndex = height / estimatedItemHeightInPixels + 1
                firstVisibleIndex set 0
                lastVisibleIndex set lastIndex
                scrollEndPosition set height
                observer.disconnect()
            }
        }
        observer.observe(this@column)
        onRemove { observer.disconnect() }

        addEventListener("scroll", {
            val scrollBottom = min(
                (it.target as HTMLDivElement).offsetHeight + (it.target as HTMLDivElement).scrollTop.toInt(),
                innerHeight.once
            )
            val scrollTop = scrollBottom - outerHeight.once
            val firstIndex = scrollPositionCache.binarySearch(scrollTop)
            val lastIndex = scrollPositionCache.binarySearch(scrollBottom)
            scrollEndPosition set scrollBottom
            firstVisibleIndex set max(firstIndex - 1, 0)
            lastVisibleIndex set min(lastIndex + 1, data.size)
        }, js("{passive:true}"))

        space { ::size { SizeConstraints(minHeight = topSpacing.current.px, maxHeight = topSpacing.current.px) } }
        forEach(
            data = { visibleItems.current },
            render = { it -> render(it) },
        )
        space { ::size { SizeConstraints(minHeight = bottomSpacing.current.px, maxHeight = bottomSpacing.current.px) } }
    } in scrolls()
}

external class ResizeObserver(callback: (Array<ResizeObserverEntry>, ResizeObserver) -> Unit) {
    fun observe(element: HTMLElement)
    fun disconnect()
}

external class ResizeObserverEntry {
    val contentRect: DOMRectReadOnly
}
