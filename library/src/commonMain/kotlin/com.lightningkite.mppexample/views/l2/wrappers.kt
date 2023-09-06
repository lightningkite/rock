package com.lightningkite.mppexample

@ViewModifierDsl3
fun ViewContext.background(
    background: Background?,
    elevation: Dimension? = null,
    padding: Insets = Insets(16.px)
): ViewWrapper {
    nativeBackground(background, elevation)
    padding(padding)
    return ViewWrapper
}

@ViewModifierDsl3
fun ViewContext.background(
    paint: Paint,
    padding: Insets = Insets(16.px)
) = background(Background(fill = paint), null, padding)
