package com.lightningkite.rock.models

import android.view.Gravity
import androidx.transition.*
import androidx.transition.Visibility.MODE_IN
import androidx.transition.Visibility.MODE_OUT

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ScreenTransition(
    val name: String,
    val enter: Transition?,
    val exit: Transition?,
    ) {
    actual companion object {
        actual val None: ScreenTransition
            get() = ScreenTransition(
                "None",
                enter = null,
                exit = null
            )
        actual val Push: ScreenTransition
            get() = ScreenTransition(
                "Push",
                enter = Slide(Gravity.LEFT),
                exit = Slide(Gravity.RIGHT)
            )
        actual val Pop: ScreenTransition
            get() = ScreenTransition(
                name = "Pop",
                enter = Slide(Gravity.RIGHT),
                exit = Slide(Gravity.LEFT)
            )
        actual val PullDown: ScreenTransition
            get() = ScreenTransition(
                name = "Pull Down",
                enter = null,
                exit = Slide(Gravity.BOTTOM)
            )
        actual val PullUp: ScreenTransition
            get() = ScreenTransition(
                name = "Pull up",
                enter = Slide(Gravity.BOTTOM),
                exit = null
            )
        actual val Fade: ScreenTransition
            get() = ScreenTransition(
                name = "Fade",
                enter = Fade(MODE_IN),
                exit = Fade(MODE_OUT)
            )
        //TODO Need to create a
        actual val GrowFade: ScreenTransition
            get() = ScreenTransition(
                "Grow Fade",
                enter = Fade(MODE_IN),
                exit = Fade(MODE_OUT)
            )
        actual val ShrinkFade: ScreenTransition
            get() = ScreenTransition(
                name = "Shrink Fade",
                enter = Fade(MODE_IN),
                exit = Fade(MODE_OUT)
            )
    }
}

