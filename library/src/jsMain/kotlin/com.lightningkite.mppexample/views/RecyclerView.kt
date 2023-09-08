package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import kotlin.math.max
import kotlin.math.min

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias RecyclerView = HTMLDivElement

private fun Dimension.toInt(): Int = value.replace("px", "").toInt()

@ViewDsl
actual fun <T> ViewContext.recyclerView(
    data: List<T>,
    render: NView.(T) -> Unit,
    height: Dimension,
    itemHeight: Dimension,
): Unit {
    val initialItems = data.subList(
        0,
        (height.toInt() / itemHeight.toInt()) + 1
    )
    val topPadding = Property(0.px)
    val visibleItems = Property<List<T>>(initialItems)
    val bottomPadding = Property(
        ((data.size - initialItems.size) * itemHeight.toInt()).px
    )

    column {
        addEventListener("scroll", {
            val scrollTop = (it.target as HTMLDivElement).scrollTop
            val index = (scrollTop / itemHeight.toInt()).toInt()
            val newItems = data.subList(
                max(index, 0),
                min(index + (height.toInt() / itemHeight.toInt()) + 1, data.lastIndex)
            )
            visibleItems set newItems
            topPadding set (index * itemHeight.toInt()).px
            bottomPadding set ((data.size - newItems.size - index) * itemHeight.toInt()).px
        }, js("{passive:true}"))

        space {
            ::size {
                SizeConstraints(
                    minHeight = topPadding.current,
                    maxHeight = topPadding.current,
                )
            }
        }
        forEach(
            data = { visibleItems.current },
            render = { item ->
                render(item) in sizedBox(
                    SizeConstraints(
                        height = itemHeight,
                        minHeight = itemHeight,
                        maxHeight = itemHeight
                    )
                )
            },
        )
        space {
            ::size {
                SizeConstraints(
                    minHeight = bottomPadding.current,
                    maxHeight = bottomPadding.current,
                )
            }
        }
    } in scrolls() in sizedBox(
        SizeConstraints(
            minHeight = height,
            maxHeight = height,
        )
    )
}
