package com.lightningkite.mppexample

@ViewDsl
fun <T> ViewContext.autoComplete(
    label: String = "",
    hint: String = "",
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
) {
    column {
        if (label != "")
            caption {
                selectable = false
                content = label
            } in padding(bottom = 4.px)
        nativeAutoComplete {
            autoCompleteHint = hint
            bind(
                options = options,
                prop = prop,
                getKey = getKey,
                getLabel = getLabel,
            )
        } in padding(8.px) in interactive(
            background = Background(
                corners = CornerRadii(8.px),
                stroke = Color.gray(0.7f),
                strokeWidth = 1.px,
            ), focusedBackground = Background(
                stroke = theme.primary.background.closestColor(),
                strokeWidth = 1.px,
            )
        )
    }
}
