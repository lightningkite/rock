package com.lightningkite.mppexample

enum class ScreenTransition {
    None,
    Push,
    Pop,
    PullDown,
    PullUp,
    Fade,
    GrowFade,
    ShrinkFade,
}

data class ScreenTransitions(
    val forward: ScreenTransition = ScreenTransition.Push,
    val reverse: ScreenTransition = ScreenTransition.Pop
)
