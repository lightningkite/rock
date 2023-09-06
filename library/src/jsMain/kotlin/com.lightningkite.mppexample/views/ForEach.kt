package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ForEach = HTMLFormElement

@ViewDsl
actual inline fun <T> ViewContext.forEach(
    crossinline data: ReactiveScope.() -> List<T>,
    crossinline render: NView.(T) -> Unit,
    crossinline fallback: NView.() -> Unit,
    noinline separator: (NView.() -> Unit)?,
    direction: ForEachDirection
): Unit = forEach(
    data = data,
    render = { _, item -> render(item) },
    fallback = fallback,
    separator = separator,
    direction = direction
)

@ViewDsl
actual inline fun <T> ViewContext.forEach(
    crossinline data: ReactiveScope.() -> List<T>,
    crossinline render: NView.(Int, T) -> Unit,
    crossinline fallback: NView.() -> Unit,
    noinline separator: (NView.() -> Unit)?,
    direction: ForEachDirection
) {
    val theme = this.theme

    box {
        var container = this as HTMLElement
        reactiveScope {
            val items = data()
            val newContainer = document.createElement("div") as HTMLDivElement
            newContainer.style.display = "flex"
            newContainer.style.flexDirection = when (direction) {
                ForEachDirection.Horizontal -> "row"
                ForEachDirection.Vertical -> "column"
            }
            withTheme(theme) {
                element(newContainer) {
                    if (items.isEmpty()) {
                        fallback()
                    } else
                        items.forEachIndexed { index, item ->
                            render(index, item)
                            if (index != items.lastIndex && separator != null)
                                separator()
                        }
                }
            }
            container.replaceWith(newContainer)
            container = newContainer
        }
    }
}

//
//actual inline fun <T> FLEXBOX.setChildren(
//    crossinline data: ReactiveScope.() -> List<T>,
//    getKey: (T) -> String,
//    crossinline render: NView.(T) -> Unit
//): Unit = element<HTMLFormElement>("div") {
//    reactiveScope {
//        val items = data()
//        innerHTML = ""
//        items.forEach {
//            render(it)
//        }
//    }
//}
//
//fun <T> RECYCLER.setAdapter(
//    crossinline data: ReactiveScope.() -> List<T>,
//    crossinline makeCell: NView.(Readable<T>) -> Unit
//) {
//    reactiveScope {
//        val items = data()
//        innerHTML = ""
//        items.forEach {
//            render(it)
//        }
//    }
//}