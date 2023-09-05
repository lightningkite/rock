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

fun ViewContext.integerInput(
    label: String,
    hint: String = "",
    value: Writable<Int>,
) = integerInput(
    label = { label },
    hint = { hint },
    value = value
)

fun ViewContext.integerInput(
    label: ReactiveScope.() -> String,
    hint: ReactiveScope.() -> String = { "" },
    value: Writable<Int>,
) {
    val text = Property(value.once.toString())

    reactiveScope {
        val textValue = text.current
        val intValue = textValue.toIntOrNull()
        println("running intinput.setvalue")
        if (intValue != null) {
            println("setting value")
            value set intValue
        }
    }

    reactiveScope {
        val intValue = value.current
        val textValue = text.once
        println("running intinput.settext")
        if (textValue != intValue.toString()) {
            println("setting text")
            text set intValue.toString()
        }
    }

    textField(
        label = label,
        hint = hint,
        text = text,
        keyboardHints = KeyboardHints.integer
    )
}
