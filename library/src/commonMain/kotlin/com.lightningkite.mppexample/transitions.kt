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
    val forward: ScreenTransition,
    val reverse: ScreenTransition
) {
    companion object {
        val None = ScreenTransitions(
            forward = ScreenTransition.None,
            reverse = ScreenTransition.None
        )

        val PushPop = ScreenTransitions(
            forward = ScreenTransition.Push,
            reverse = ScreenTransition.Pop
        )
    }
}
