package com.lightningkite.mppexample

import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ForEach = HTMLFormElement

actual inline fun <T> ViewContext.forEach(
    crossinline data: ReactiveScope.() -> List<T>,
    crossinline render: NView.(T) -> Unit
): Unit = element<HTMLFormElement>("div") {
    reactiveScope {
        val items = data()
        innerHTML = ""
        items.forEach {
            render(it)
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