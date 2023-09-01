package com.lightningkite.mppexample


fun ViewContext.buttonLike(color: Color, disabledColor: Color): ViewWrapper {
    val hover = color.darken(0.15f)
    val focus = color.darken(0.3f)

    interactive(
        background = Background(
            fill = color,
            stroke = hover,
            strokeWidth = 2.px,
            corners = CornerRadii(12.px),
        ),
        elevation = 2.px,
        hoverBackground = Background(fill = hover.toGradient()),
        hoverElevation = 4.px,
        downBackground = Background(fill = focus),
        downElevation = 8.px,
        disabledBackground = Background(fill = disabledColor, stroke = disabledColor.darken(0.15f)),
        disabledElevation = 0.px
    )
    return ViewWrapper
}
