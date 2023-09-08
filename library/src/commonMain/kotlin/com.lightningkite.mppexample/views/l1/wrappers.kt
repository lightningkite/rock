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
expect fun ViewContext.stackCenterLeft(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.stackCenterRight(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.stackRight(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.stackLeft(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.stackTop(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.stackBottom(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignTop(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignBottom(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.weight(amount: Float): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.fullWidth(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.fullHeight(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(insets: Insets = Insets.zero()): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(insets: Dimension): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(horizontal: Dimension = 0.px, vertical: Dimension = 0.px): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(
    left: Dimension = 0.px,
    top: Dimension = 0.px,
    right: Dimension = 0.px,
    bottom: Dimension = 0.px
): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(insets: Insets = Insets.zero()): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(horizontal: Dimension = 0.px, vertical: Dimension = 0.px): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(
    left: Dimension = 0.px,
    top: Dimension = 0.px,
    right: Dimension = 0.px,
    bottom: Dimension = 0.px
): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(insets: Dimension): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.nativeBackground(background: Background? = null, elevation: Dimension? = null): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.nativeBackground(paint: Paint): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.nativeChangingBackground(getBackground: ReactiveScope.() -> Background): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.interactive(
    transitions: Boolean = true,
    background: Background? = null,
    hoverBackground: Background? = null,
    downBackground: Background? = null,
    focusedBackground: Background? = null,
    disabledBackground: Background? = null,
    elevation: Dimension? = null,
    hoverElevation: Dimension? = null,
    downElevation: Dimension? = null,
    focusedElevation: Dimension? = null,
    disabledElevation: Dimension? = null
): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.changingInteractive(
    transitions: Boolean = true,
    background: (ReactiveScope.() -> Background)? = null,
    hoverBackground: (ReactiveScope.() -> Background)? = null,
    downBackground: (ReactiveScope.() -> Background)? = null,
    focusedBackground: (ReactiveScope.() -> Background)? = null,
    disabledBackground: (ReactiveScope.() -> Background)? = null,
    elevation: (ReactiveScope.() -> Dimension)? = null,
    hoverElevation: (ReactiveScope.() -> Dimension)? = null,
    downElevation: (ReactiveScope.() -> Dimension)? = null,
    focusedElevation: (ReactiveScope.() -> Dimension)? = null,
    disabledElevation: (ReactiveScope.() -> Dimension)? = null,
): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.scrolls(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.scrollsHorizontally(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.clickable(
    enabled: Readable<Boolean>,
    onClick: suspend () -> Unit,
): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.clickable(
    onClick: suspend () -> Unit,
): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.ignoreInteraction(): ViewWrapper
