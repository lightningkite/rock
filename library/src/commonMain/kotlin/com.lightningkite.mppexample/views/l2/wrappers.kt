package com.lightningkite.mppexample

fun ViewContext.background(
    background: Background?,
    elevation: Dimension?,
    padding: Insets = Insets(16.px)
): ViewWrapper {
    nativeBackground(background, elevation)
    padding(padding)
    return ViewWrapper
}

fun ViewContext.background(
    paint: Paint,
    padding: Insets = Insets(16.px)
) = background(Background(fill = paint), null, padding)
