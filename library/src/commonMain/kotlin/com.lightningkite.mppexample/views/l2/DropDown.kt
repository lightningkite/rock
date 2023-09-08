package com.lightningkite.mppexample

@ViewDsl
fun <T> ViewContext.dropDown(
    options: Readable<List<T>>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T>,
    disabled: ReactiveScope.() -> Boolean = { false },
) {
    nativeDropDown(
        options = options,
        getLabel = getLabel,
        getKey = getKey,
        prop = prop,
    ) {
        ::disabled { disabled() }
    } in padding(8.px) in interactive(
        background = Background(
            fill = theme.normal.background,
            strokeWidth = 1.px,
            stroke = Color.gray(0.7f),
            corners = CornerRadii(8.px)
        ),
        downBackground = Background(
            strokeWidth = 1.px,
            stroke = theme.primary.background.closestColor().lighten(0.6f),
        ),
        disabledBackground = Background(
            fill = theme.normalDisabled?.background,
            stroke = Color.gray(0.8f),
            strokeWidth = 1.px,
        )
    )
}
