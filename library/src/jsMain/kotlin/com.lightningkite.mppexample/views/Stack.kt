package com.lightningkite.mppexample

import org.w3c.dom.HTMLTableRowElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Stack = HTMLTableRowElement

actual inline fun ViewContext.stack(setup: Stack.() -> Unit): Unit = element<HTMLTableRowElement>("div") {
    className = "rock-stack"
    setup()
}

//actual var Stack.gravity: StackGravity
//    get() = throw NotImplementedError()
//    set(value) {
//        classList.remove("rock-stack-gravity-start", "rock-stack-gravity-center", "rock-stack-gravity-end")
//        classList.add(
//            when (value) {
//                StackGravity.Start -> "rock-stack-gravity-start"
//                StackGravity.Center -> "rock-stack-gravity-center"
//                StackGravity.End -> "rock-stack-gravity-end"
//            }
//        )
//    }
//
//actual var Stack.direction: StackDirection
//    get() = throw NotImplementedError()
//    set(value) {
//        if (value == StackDirection.Z) {
//            classList.add("rock-z-stack")
//        } else {
//            classList.remove("rock-z-stack")
//            style.flexDirection = when (value) {
//                StackDirection.Row -> "row"
//                StackDirection.Column -> "column"
//                StackDirection.Z -> throw IllegalArgumentException()
//            }
//        }
//    }
