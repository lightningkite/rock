package com.lightningkite.mppexample

fun ViewContext.appBar(
    title: ReactiveScope.() -> String, showBackButton: Boolean = true, setup: NView.() -> Unit = {}
) {
    withTheme(theme.primaryTheme()) {
        row {
            gravity = StackGravity.Center

            if (showBackButton) image {
                source = ImageVector(
                    width = 24.px, height = 24.px, viewBoxWidth = 1024, viewBoxHeight = 1024, paths = listOf(
                        ImageVector.Path(
                            path = "M669.6 849.6c8.8 8 22.4 7.2 30.4-1.6s7.2-22.4-1.6-30.4l-309.6-280c-8-7.2-8-17.6 0-24.8l309.6-270.4c8.8-8 9.6-21.6 2.4-30.4-8-8.8-21.6-9.6-30.4-2.4L360.8 480.8c-27.2 24-28 64-0.8 88.8l309.6 280z",
                            fillColor = theme.normal.foreground.closestColor(),
                            strokeWidth = 96,
                            strokeColor = theme.normal.foreground.closestColor()
                        )
                    )
                )
            } in interactive(
                background = Background(
                    corners = CornerRadii(8.px)
                ),
                downBackground = Background(
                    fill = theme.normal.background.closestColor().darken(0.1f),
                )
            ) in margin(Insets(right = 16.px)) in clickable {
                navigator.goBack()
            }

            h3 {
                ::content { title() }
            }
        } in background(theme.normal.background)
    } in margin(Insets(bottom = 16.px))
}

fun ViewContext.appBar(title: String, showBackButton: Boolean = true, setup: NView.() -> Unit = {}) =
    appBar(title = { title }, showBackButton = showBackButton, setup = setup)
