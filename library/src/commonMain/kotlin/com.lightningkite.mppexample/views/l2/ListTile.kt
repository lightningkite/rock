package com.lightningkite.mppexample

val BORDER_COLOR = Color.gray(0.8f)

@ViewDsl
fun <T> ViewContext.listTileGroup(
    data: ReactiveScope.() -> List<T>,
    render: NView.(T) -> Unit,
    fallback: NView.() -> Unit = {},
) {
    val enabled = SharedReadable { data().isNotEmpty() }

    column {
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeChangingBackground {
            Background(
                fill = if (enabled.current) BORDER_COLOR else Color.transparent
            )
        }
        forEach(
            data = data, render = render, fallback = fallback
        )
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeChangingBackground {
            Background(
                fill = if (enabled.current) BORDER_COLOR else Color.transparent
            )
        }
    }
}

@ViewDsl
fun ViewContext.listTileGroup(setup: NView.() -> Unit) {
    column {
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeBackground(BORDER_COLOR)
        setup()
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeBackground(BORDER_COLOR)
    }
}

@ViewDsl
fun ViewContext.listTile(onClick: (() -> Unit)? = null, setup: NView.() -> Unit) {
    row {
        gravity = RowGravity.Center

        setup()

        if (onClick != null) {
            space() in weight(1f)
            image {
                source = ImageVector(
                    width = 32.px, height = 32.px, viewBoxWidth = 32, viewBoxHeight = 32, paths = listOf(
                        ImageVector.Path(
                            path = "M22.314 16l-8.485 8.485-2.829-2.828 5.657-5.657-5.657-5.657 2.828-2.828 8.486 8.485z",
                            fillColor = Color.black,
                        )
                    )
                )
            }
        }
    } in padding(16.px) in wrapIfElse(onClick != null, wrapper = {
        clickable(onClick!!)
        interactive(
            background = Background(
                fill = theme.normal.background,
                stroke = BORDER_COLOR,
                strokeWidth = 1.px,
            ), hoverBackground = Background(
                fill = theme.normal.background.closestColor().darken(0.1f),
                stroke = BORDER_COLOR,
                strokeWidth = 1.px,
            ), downBackground = Background(
                fill = theme.normal.background.closestColor().darken(0.2f),
                stroke = BORDER_COLOR,
                strokeWidth = 1.px,
            )
        )
    }, elseWrapper = {
        nativeBackground(
            Background(
                fill = theme.normal.background,
                stroke = BORDER_COLOR,
                strokeWidth = 1.px,
            )
        )
    })
}

@ViewDsl
fun ViewContext.listTile(
    image: ImageSource,
    imageConstraints: SizeConstraints? = null,
    imageAlpha: Double = 0.25,
    onClick: (() -> Unit)? = null,
    setup: NView.() -> Unit
) {
    stack {
        image {
            alpha = imageAlpha
            source = image
            scaleType = ImageMode.Crop
        } in fullWidth() in sizedBox(constraints = imageConstraints ?: SizeConstraints())
        withTheme(theme.copy(normal = theme.normal.copy(background = Color.transparent))) {
            listTile(onClick = onClick) {
                column {
                    setup()
                } in alignCenter() in padding(left = 16.px)
            }
        }
    } in interactive(
        background = Background(
            fill = theme.normal.background,
        ), hoverBackground = Background(
            fill = theme.normal.background.closestColor().darken(0.1f),
        ), downBackground = Background(
            fill = theme.normal.background.closestColor().darken(0.2f),
        )
    )
}
