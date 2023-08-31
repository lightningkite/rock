package com.lightningkite.mppexample

enum class HeadingLevel { H1, H2, H3, H4, H5, H6, Body }

fun HeadingLevel.toMultiplier(): Float = when (this) {
    HeadingLevel.Body -> 1f
    HeadingLevel.H6 -> 1.2f
    HeadingLevel.H5 -> 1.4f
    HeadingLevel.H4 -> 1.6f
    HeadingLevel.H3 -> 1.8f
    HeadingLevel.H2 -> 2f
    HeadingLevel.H1 -> 2.2f
}

inline fun ViewContext.text(level: HeadingLevel, crossinline setup: Text.() -> Unit) {
    val textSetup: Text.() -> Unit = {
        textStyle = TextStyle(
            size = theme.baseSize * level.toMultiplier(),
            color = theme.normal.foreground.closestColor(),
            font = theme.bodyFont,
            allCaps = theme.allCaps
        )
        setup()
    }
    when (level) {
        HeadingLevel.H1 -> nativeH1(textSetup)
        HeadingLevel.H2 -> nativeH2(textSetup)
        HeadingLevel.H3 -> nativeH3(textSetup)
        HeadingLevel.H4 -> nativeH4(textSetup)
        HeadingLevel.H5 -> nativeH5(textSetup)
        HeadingLevel.H6 -> nativeH6(textSetup)
        HeadingLevel.Body -> nativeText(textSetup)
    }
}

inline fun ViewContext.text(crossinline setup: Text.() -> Unit) = text(HeadingLevel.Body, setup)
