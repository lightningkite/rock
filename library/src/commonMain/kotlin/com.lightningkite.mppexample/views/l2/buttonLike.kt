package com.lightningkite.mppexample


fun ViewContext.buttonLike(color: Color): ViewWrapper {
    val hover = color.darken(0.15f)
    val focus = color.darken(0.3f)
    withBackground(
        Background(
            fill = color,
//            fill = color.toGradient(),
            stroke = hover,
            strokeWidth = 2.px,
            corners = CornerRadii(12.px),
        )
    )
    hoverable(
        elevation = 2.px,
        background = Background(
            fill = hover//.toGradient()
        )
    )
    focusable(
        elevation = 4.px,
        background = Background(
            fill = focus//.toGradient()
        )
    )
    return ViewWrapper
}

