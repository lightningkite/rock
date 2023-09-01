package com.lightningkite.mppexample


@ViewModifierDsl3
expect fun ViewContext.alignLeft(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignRight(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignCenter(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.stackCenter(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignTop(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignBottom(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.weight(amount: Float): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(insets: Insets = Insets.zero()): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(insets: Dimension): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(insets: Insets = Insets.zero()): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(insets: Dimension): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.nativeBackground(background: Background? = null, elevation: Dimension? = null): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.nativeBackground(paint: Paint): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.changingBackground(getBackground: ReactiveScope.() -> Background): ViewWrapper

//@ViewModifierDsl3
//expect fun ViewContext.hoverable(background: Background? = null, elevation: Dimension? = null): ViewWrapper
//
//@ViewModifierDsl3
//expect fun ViewContext.focusable(background: Background? = null, elevation: Dimension? = null): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.interactive(
    transitions: Boolean = true,
    background: Background? = null,
    hoverBackground: Background? = null,
    downBackground: Background? = null,
    disabledBackground: Background? = null,
    elevation: Dimension? = null,
    hoverElevation: Dimension? = null,
    downElevation: Dimension? = null,
    disabledElevation: Dimension? = null
): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.scrolls(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.scrollsHorizontally(): ViewWrapper
