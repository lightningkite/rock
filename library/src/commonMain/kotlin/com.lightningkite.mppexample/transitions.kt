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
        val HorizontalSlide = ScreenTransitions(
            forward = ScreenTransition.Push,
            reverse = ScreenTransition.Pop
        )
        val Fade = ScreenTransitions(
            forward = ScreenTransition.Fade,
            reverse = ScreenTransition.Fade
        )
        val FadeResize = ScreenTransitions(
            forward = ScreenTransition.ShrinkFade,
            reverse = ScreenTransition.GrowFade,
        )
        val VerticalSlide = ScreenTransitions(
            forward = ScreenTransition.PullUp,
            reverse = ScreenTransition.PullDown,
        )
    }
}
