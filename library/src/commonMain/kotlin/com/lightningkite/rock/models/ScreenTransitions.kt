package com.lightningkite.rock.models

expect class ScreenTransition {
    companion object {
        val None: ScreenTransition
        val Push: ScreenTransition
        val Pop: ScreenTransition
        val PullDown: ScreenTransition
        val PullUp: ScreenTransition
        val Fade: ScreenTransition
        val GrowFade: ScreenTransition
        val ShrinkFade: ScreenTransition
    }
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
            forward = ScreenTransition.GrowFade,
            reverse = ScreenTransition.ShrinkFade,
        )
        val VerticalSlide = ScreenTransitions(
            forward = ScreenTransition.PullUp,
            reverse = ScreenTransition.PullDown,
        )
    }
}
