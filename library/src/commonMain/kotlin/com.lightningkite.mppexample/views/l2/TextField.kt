package com.lightningkite.mppexample

@ViewDsl
private fun ViewContext.textField(
    label: ReactiveScope.() -> String = { "" },
    hint: ReactiveScope.() -> String = { "" },
    text: Writable<String>,
    keyboardHints: KeyboardHints? = null,
    minValue: Double?,
    maxValue: Double?,
    leadingIcon: ImageVector? = null,
) {
    val padding = if (leadingIcon == null) Insets(8.px) else Insets(
        left = leadingIcon.width + 12.px, top = 8.px, right = 8.px, bottom = 8.px
    )

    column {
        caption {
            ::content{ label() }
            ::exists { label().isNotEmpty() }
            selectable = false
        } in padding(Insets(bottom = 4.px))

        stack {
            nativeTextField {
                bind(text)
                ::hint { hint() }
                if (keyboardHints != null) this.keyboardHints = keyboardHints
                if (minValue != null) this.minValue = minValue
                if (maxValue != null) this.maxValue = maxValue
            } in padding(padding) in interactive(
                background = Background(
                    corners = CornerRadii(8.px),
                    stroke = Color.gray(0.7f),
                    strokeWidth = 1.px,
                ), focusedBackground = Background(
                    stroke = theme.primary.background.closestColor(),
                    strokeWidth = 1.px,
                )
            )
            if (leadingIcon != null) image {
                source = leadingIcon
            } in stackCenterLeft() in margin(left = 8.px) in ignoreInteraction()
        }
    } in padding(Insets.symmetric(vertical = 8.px))
}

@ViewDsl
fun ViewContext.textField(
    label: ReactiveScope.() -> String = { "" },
    hint: ReactiveScope.() -> String = { "" },
    text: Writable<String>,
    keyboardHints: KeyboardHints? = null,
    leadingIcon: ImageVector? = null,
) = textField(
    label = label,
    hint = hint,
    text = text,
    keyboardHints = keyboardHints,
    minValue = null,
    maxValue = null,
    leadingIcon = leadingIcon,
)

@ViewDsl
fun ViewContext.integerInput(
    label: String = "",
    hint: String = "",
    value: Writable<Int>,
    min: Int? = null,
    max: Int? = null,
    leadingIcon: ImageVector? = null,
) = integerInput(
    label = { label },
    hint = { hint },
    value = value,
    min = min,
    max = max,
    leadingIcon = leadingIcon,
)

@ViewDsl
fun ViewContext.integerInput(
    label: ReactiveScope.() -> String = { "" },
    hint: ReactiveScope.() -> String = { "" },
    value: Writable<Int>,
    min: Int? = null,
    max: Int? = null,
    leadingIcon: ImageVector? = null,
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
        keyboardHints = KeyboardHints.integer,
        minValue = min?.toDouble(),
        maxValue = max?.toDouble(),
        leadingIcon = leadingIcon,
    )
}
