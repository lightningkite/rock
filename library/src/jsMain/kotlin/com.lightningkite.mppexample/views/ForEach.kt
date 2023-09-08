package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ForEach = HTMLFormElement

@ViewDsl
actual fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: ViewContext.(T) -> Unit,
    separator: (ViewContext.() -> Unit)?,
    fallback: ViewContext.() -> Unit,
): Unit = forEach(
    data = data,
    render = { _, item -> render(item) },
    fallback = fallback,
    separator = separator,
)

@ViewDsl
actual fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: ViewContext.(Int, T) -> Unit,
    separator: (ViewContext.() -> Unit)?,
    fallback: ViewContext.() -> Unit,
) {
    val container = this.stack.last()
    val theme = this.theme
    lateinit var startMarker: HTMLElement
    var endMarker: HTMLElement? = null

    box {
        startMarker = this
        exists = false
    }
    reactiveScope {
        val items = data()

//        render new items into a temporary div
        val newContainer = document.createElement("div") as HTMLDivElement
        withTheme(theme) {
            element(newContainer) {
                if (items.isEmpty())
                    fallback()
                else
                    items.forEachIndexed { index, item ->
                        render(index, item)
                        if (index != items.lastIndex && separator != null)
                            separator()
                    }
            }
        }
        val startIndex = container.children.asList().indexOf(startMarker) + 1
        val endIndex = (if (endMarker == null) startIndex else container.children.asList().indexOf(endMarker!!)) - 1

//        remove previous items
        for (i in endIndex downTo startIndex)
            container.removeChild(container.children.asList()[i])

//        move items from newContainer to the correct place in container
        var insertAfter = startMarker
        while (newContainer.children.asList().isNotEmpty()) {
            val nextChild = newContainer.children.asList().first() as HTMLElement
            insertAfter.after(nextChild)
            insertAfter = nextChild
        }
        newContainer.remove()
    }
    box {
        endMarker = this
        exists = false
    }
}
