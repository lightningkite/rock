package com.lightningkite.mppexample

enum class TextLevel { H1, H2, H3, H4, H5, H6, Body, Caption }

fun TextLevel.toMultiplier(): Float = when (this) {
    TextLevel.Caption -> 0.9f
    TextLevel.Body -> 1f
    TextLevel.H6 -> 1.2f
    TextLevel.H5 -> 1.4f
    TextLevel.H4 -> 1.6f
    TextLevel.H3 -> 1.8f
    TextLevel.H2 -> 2f
    TextLevel.H1 -> 2.2f
}

inline fun ViewContext.text(level: TextLevel, crossinline setup: Text.() -> Unit) {
    val textSetup: Text.() -> Unit = {
        if (theme.allCaps)
            println("ALL CAPS")
        textStyle = TextStyle(
            size = theme.baseSize * level.toMultiplier(),
            color = if (level == TextLevel.Caption) theme.normal.foreground.closestColor()
                .lighten(0.4f) else theme.normal.foreground.closestColor(),
            disabledColor = theme.normalDisabled.foreground.closestColor(),
            font = theme.bodyFont,
            allCaps = theme.allCaps
        )

        setup()
    }
    when (level) {
        TextLevel.H1 -> nativeH1(textSetup)
        TextLevel.H2 -> nativeH2(textSetup)
        TextLevel.H3 -> nativeH3(textSetup)
        TextLevel.H4 -> nativeH4(textSetup)
        TextLevel.H5 -> nativeH5(textSetup)
        TextLevel.H6 -> nativeH6(textSetup)
        TextLevel.Body -> nativeText(textSetup)
        TextLevel.Caption -> nativeText(textSetup)
    }
}

inline fun ViewContext.caption(crossinline setup: Text.() -> Unit) = text(TextLevel.Caption, setup)
inline fun ViewContext.text(crossinline setup: Text.() -> Unit) = text(TextLevel.Body, setup)
inline fun ViewContext.h1(crossinline setup: Text.() -> Unit) = text(TextLevel.H1, setup)
inline fun ViewContext.h2(crossinline setup: Text.() -> Unit) = text(TextLevel.H2, setup)
inline fun ViewContext.h3(crossinline setup: Text.() -> Unit) = text(TextLevel.H3, setup)
inline fun ViewContext.h4(crossinline setup: Text.() -> Unit) = text(TextLevel.H4, setup)
inline fun ViewContext.h5(crossinline setup: Text.() -> Unit) = text(TextLevel.H5, setup)
inline fun ViewContext.h6(crossinline setup: Text.() -> Unit) = text(TextLevel.H6, setup)
