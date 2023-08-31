package com.lightningkite.mppexample


inline fun ViewContext.text(crossinline setup: Text.() -> Unit) {
    nativeText {
        textStyle = TextStyle(
            size = theme.baseSize,
            color = theme.normal.foreground.closestColor(),
            font = theme.bodyFont,
            allCaps = theme.allCaps
        )
        setup()
    }
}
