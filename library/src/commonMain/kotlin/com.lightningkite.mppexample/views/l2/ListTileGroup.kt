package com.lightningkite.mppexample

@ViewDsl
fun <T> ViewContext.listTileGroup(
    data: ReactiveScope.() -> List<T>,
    render: ViewContext.(T) -> Unit,
    fallback: ViewContext.() -> Unit = {},
) {
    val enabled = SharedReadable { data().isNotEmpty() }

    column {
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeChangingBackground {
            Background(
                fill = if (enabled.current) LIST_TILE_BORDER_COLOR else Color.transparent
            )
        }
        forEach(
            data = data, render = render, fallback = fallback
        )
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeChangingBackground {
            Background(
                fill = if (enabled.current) LIST_TILE_BORDER_COLOR else Color.transparent
            )
        }
    }
}

@ViewDsl
fun ViewContext.listTileGroup(setup: NView.() -> Unit) {
    column {
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeBackground(LIST_TILE_BORDER_COLOR)
        setup()
        space() in fullWidth() in sizedBox(SizeConstraints(minHeight = 1.px)) in nativeBackground(LIST_TILE_BORDER_COLOR)
    }
}
