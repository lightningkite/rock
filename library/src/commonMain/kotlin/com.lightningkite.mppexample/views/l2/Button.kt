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

fun ViewContext.button(
    options: ButtonOptions,
    buttonSetup: NativeButton.() -> Unit,
) {
    var buttonTheme = when (options.palette) {
        ButtonPalette.Primary -> theme.primaryTheme()
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
    val foreground = when (options.variant) {
        ButtonVariant.Text -> buttonTheme.normal.background.closestColor()
        ButtonVariant.Outlined -> outline!!
        else -> buttonTheme.normal.foreground.closestColor()
    }
    val elevation = if (options.variant == ButtonVariant.Contained) 2.px else 0.px
    val hoverElevation = if (options.variant == ButtonVariant.Contained) 4.px else 0.px
    val downElevation = if (options.variant == ButtonVariant.Contained) 8.px else 0.px
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

    val setup = {
        nativeButton {
            cursor = "pointer"
            withRenderContext(RenderContext.Button) {
                withTheme(buttonTheme) {
                    buttonSetup()
                }
            }
        } in padding(
            Insets.symmetric(
                horizontal = (16 * sizeMultiplier).toInt().px,
                vertical = (8 * sizeMultiplier).toInt().px
            )
        ) in interactive(
            background = Background(
                fill = background,
                stroke = outline,
                strokeWidth = if (outline != null) 1.px else 0.px,
                corners = CornerRadii(8.px),
            ),
            elevation = elevation,
            hoverBackground = Background(fill = hoverBackground),
            hoverElevation = hoverElevation,
            downBackground = Background(fill = downBackground),
            downElevation = downElevation,
            disabledBackground = Background(
                fill = buttonTheme.normalDisabled.background,
                stroke = outline?.toGrayscale(),
            ),
            disabledElevation = 0.px
        )
    }

    if (options.fullWidth) setup() else box {
        setup()
    }
}

fun ViewContext.button(buttonSetup: NativeButton.() -> Unit) =
    button(options = ButtonOptions(), buttonSetup = buttonSetup)
