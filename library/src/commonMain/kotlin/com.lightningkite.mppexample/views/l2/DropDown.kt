package com.lightningkite.mppexample

@ViewDsl
fun <T> ViewContext.dropDown(
    options: Readable<List<T>>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T>,
) {
    nativeDropDown(
        options = options,
        getLabel = getLabel,
        getKey = getKey,
        prop = prop,
    ) in padding(8.px) in interactive(
        background = Background(
            fill = theme.normal.background,
            strokeWidth = 1.px,
            stroke = theme.primaryDisabled.foreground.closestColor(),
            corners = CornerRadii(8.px)
        ),
        downBackground = Background(
            strokeWidth = 1.px,
            stroke = theme.primary.background.closestColor().lighten(0.6f),
        ),
    )
}
