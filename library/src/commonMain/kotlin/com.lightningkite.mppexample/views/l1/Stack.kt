package com.lightningkite.mppexample

@ViewDsl
expect fun ViewContext.stack(setup: Stack.() -> Unit = {}): Unit
expect class Stack : NView

//expect var Stack.gravity: StackGravity
//
enum class StackGravity {
    Start, Center, End
}
