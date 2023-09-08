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

@ViewDsl
inline fun ViewContext.text(level: TextLevel, crossinline setup: NativeText.() -> Unit) {
    val textSetup: NativeText.() -> Unit = {
        textStyle = TextStyle(
            size = theme.baseSize * level.toMultiplier(),
            color = if (level == TextLevel.Caption) theme.normal.foreground.closestColor()
                .lighten(0.4f) else theme.normal.foreground.closestColor(),
            disabledColor = theme.normalDisabled?.foreground?.closestColor() ?: theme.normal.foreground.closestColor(),
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

@ViewDsl
inline fun ViewContext.caption(crossinline setup: NativeText.() -> Unit) = text(TextLevel.Caption, setup)

@ViewDsl
fun ViewContext.caption(text: String) = caption { content = text }

@ViewDsl
inline fun ViewContext.text(crossinline setup: NativeText.() -> Unit) = text(TextLevel.Body, setup)

@ViewDsl
fun ViewContext.text(text: String) = text { content = text }

@ViewDsl
inline fun ViewContext.h1(crossinline setup: NativeText.() -> Unit) = text(TextLevel.H1, setup)

@ViewDsl
fun ViewContext.h1(text: String) = h1 { content = text }

@ViewDsl
inline fun ViewContext.h2(crossinline setup: NativeText.() -> Unit) = text(TextLevel.H2, setup)

@ViewDsl
fun ViewContext.h2(text: String) = h2 { content = text }

@ViewDsl
inline fun ViewContext.h3(crossinline setup: NativeText.() -> Unit) = text(TextLevel.H3, setup)

@ViewDsl
fun ViewContext.h3(text: String) = h3 { content = text }

@ViewDsl
inline fun ViewContext.h4(crossinline setup: NativeText.() -> Unit) = text(TextLevel.H4, setup)

@ViewDsl
fun ViewContext.h4(text: String) = h4 { content = text }

@ViewDsl
inline fun ViewContext.h5(crossinline setup: NativeText.() -> Unit) = text(TextLevel.H5, setup)

@ViewDsl
fun ViewContext.h5(text: String) = h5 { content = text }

@ViewDsl
inline fun ViewContext.h6(crossinline setup: NativeText.() -> Unit) = text(TextLevel.H6, setup)

@ViewDsl
fun ViewContext.h6(text: String) = h6 { content = text }
