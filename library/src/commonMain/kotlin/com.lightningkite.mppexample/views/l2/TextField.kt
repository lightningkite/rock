package com.lightningkite.mppexample

fun ViewContext.textField(
    label: ReactiveScope.() -> String,
    hint: ReactiveScope.() -> String = { "" },
    text: Writable<String>,
    keyboardHints: KeyboardHints? = null
) {
    column {
        caption {
            ::content{ label() }
            selectable = false
        } in padding(Insets(bottom = 4.px))
        nativeTextField {
            bind(text)
            ::hint { hint() }
            if (keyboardHints != null)
                this.keyboardHints = keyboardHints
        } in padding(8.px) in interactive(
            background = Background(
                corners = CornerRadii(8.px),
                stroke = Color.gray(0.7f),
                strokeWidth = 1.px,
            ),
            focusedBackground = Background(
                stroke = theme.primary.background.closestColor(),
                strokeWidth = 1.px,
            )
        )
    } in padding(Insets.symmetric(vertical = 8.px))
}
