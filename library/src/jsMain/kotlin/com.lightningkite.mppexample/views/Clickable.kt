package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

//@Suppress("ACTUAL_WITHOUT_EXPECT")
//actual typealias Clickable = HTMLElement
//
//actual inline fun ViewContext.clickable(
//    enabled: ReactiveScope.() -> Boolean,
//    crossinline onClick: suspend () -> Unit,
//    setup: NView.() -> Unit
//): Unit = element<HTMLDivElement>("div") {
//
//
//    setup()
//}
//
//actual fun NativeButton.onClick(action: () -> Unit) {
//    addEventListener("click", {
//        action()
//    })
//}
//
//actual var NativeButton.clickable: Boolean
//    get() = throw NotImplementedError()
//    set(value) {
//        disabled = !value
//    }
