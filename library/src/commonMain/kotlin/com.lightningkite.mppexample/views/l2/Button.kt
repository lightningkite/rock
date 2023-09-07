package com.lightningkite.mppexample

enum class ButtonVariant { Contained, Outlined, Text }
enum class ButtonSize { Small, Medium, Large }
enum class ButtonPalette { Primary, Accent, Danger }

data class ButtonOptions(
    val variant: ButtonVariant = ButtonVariant.Contained,
    val size: ButtonSize = ButtonSize.Medium,
    val palette: ButtonPalette = ButtonPalette.Primary,
    val fullWidth: Boolean = false,
)

@ViewDsl
fun ViewContext.button(
    options: ButtonOptions,
    onClick: suspend () -> Unit,
    disabled: ReactiveScope.() -> Boolean = { false },
    loading: Writable<Boolean>? = null,
    setup: NView.() -> Unit,
) {
    var buttonTheme = when (options.palette) {
        ButtonPalette.Primary -> theme.primaryTheme(allCaps = true)
        ButtonPalette.Accent -> theme.accentTheme()
        ButtonPalette.Danger -> theme.copy(
            normal = PaintPair(
                foreground = Color(1f, 1f, 1f, 1f),
                background = Color.fromHex(0xD32F2F),
            ),
            normalDisabled = PaintPair(
                foreground = Color.fromHex(0xededed),
                background = Color.fromHex(0x666666)
            ),
            allCaps = true
        )
    }

    val sizeMultiplier = when (options.size) {
        ButtonSize.Small -> 0.75
        ButtonSize.Medium -> 1.0
        ButtonSize.Large -> 1.5
    }

    val background =
        if (options.variant == ButtonVariant.Contained) buttonTheme.normal.background.closestColor() else Color.transparent
    val outline: Color? =
        if (options.variant == ButtonVariant.Outlined) buttonTheme.normal.background.closestColor() else null
    val hoverOutline: Color? = outline?.darken(0.15f)
    val downOutline: Color? = outline?.darken(0.3f)
    val foreground = when (options.variant) {
        ButtonVariant.Text -> buttonTheme.normal.background.closestColor()
        ButtonVariant.Outlined -> outline!!
        else -> buttonTheme.normal.foreground.closestColor()
    }
    val elevation = if (options.variant == ButtonVariant.Contained) 2.px else 0.px
    val hoverElevation = if (options.variant == ButtonVariant.Contained) 4.px else 0.px
    val downElevation = if (options.variant == ButtonVariant.Contained) 6.px else 0.px
    val hoverBackground = background.darken(0.15f)
    val downBackground = if (options.variant == ButtonVariant.Contained)
        background.darken(0.3f)
    else
        foreground.lighten(0.9f)

    buttonTheme = buttonTheme.copy(
        baseSize = buttonTheme.baseSize * sizeMultiplier,
        normal = PaintPair(foreground = foreground, background = background),
        normalDisabled = PaintPair(
            foreground = foreground.toGrayscale(),
            background = background.toGrayscale()
        )
    )

    val loadingProp = loading ?: Property(false)

    val setupAll = {
        nativeButton {
            onClick {
                launch {
                    loadingProp set true
                    try {
                        onClick()
                    } finally {
                        loadingProp set false
                    }
                }
            }
            ::clickable { !loadingProp.current && !disabled() }

            cursor = "pointer"
            withRenderContext(ButtonRenderContext(size = options.size)) {
                withTheme(buttonTheme) {
                    stack {
                        activityIndicator(visible = loadingProp) in stackCenter()
                        row {
                            gravity = RowGravity.Center
                            ::visible { !loadingProp.current }
                            setup()
                        } in stackCenter()
                    }
                }
            }
        } in padding(
            Insets.symmetric(
                horizontal = (16 * sizeMultiplier).toInt().px,
                vertical = (8 * sizeMultiplier).toInt().px
            )
        ) in margin(
            Insets.symmetric(vertical = 8.px)
        ) in interactive(
            background = Background(
                fill = background,
                stroke = outline,
                strokeWidth = if (outline != null) 1.px else 0.px,
                corners = CornerRadii(8.px),
            ),
            elevation = elevation,
            hoverBackground = Background(
                fill = hoverBackground,
                stroke = hoverOutline,
                strokeWidth = if (hoverOutline != null) 1.px else 0.px
            ),
            hoverElevation = hoverElevation,
            downBackground = Background(
                fill = downBackground, stroke = downOutline,
                strokeWidth = if (downOutline != null) 1.px else 0.px
            ),
            downElevation = downElevation,
            disabledBackground = Background(
                fill = buttonTheme.normalDisabled.background,
                stroke = outline?.toGrayscale(),
                strokeWidth = if (outline != null) 1.px else 0.px
            ),
            disabledElevation = 0.px
        )
    }

    if (options.fullWidth) setupAll() else box {
        setupAll()
    }
}

@ViewDsl
fun ViewContext.button(
    onClick: suspend () -> Unit, disabled: ReactiveScope.() -> Boolean,
    loading: Writable<Boolean>? = null,
    setup: NView.() -> Unit
) =
    button(
        options = ButtonOptions(),
        disabled = disabled,
        loading = loading,
        onClick = onClick,
        setup = setup,
    )

@ViewDsl
fun ViewContext.button(onClick: suspend () -> Unit, loading: Writable<Boolean>? = null, setup: NView.() -> Unit) =
    button(
        options = ButtonOptions(),
        disabled = { false },
        onClick = onClick,
        setup = setup,
    )

@ViewDsl
fun ViewContext.textButton(
    onClick: suspend () -> Unit,
    disabled: ReactiveScope.() -> Boolean = { false },
    setup: NView.() -> Unit
) =
    button(
        options = ButtonOptions(variant = ButtonVariant.Text),
        disabled = disabled,
        onClick = onClick,
        setup = setup,
    )
