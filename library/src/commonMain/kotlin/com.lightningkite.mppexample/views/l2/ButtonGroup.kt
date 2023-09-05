package com.lightningkite.mppexample

data class ButtonGroupItem(
    val text: String,
    val onClick: suspend () -> Unit
)

fun ViewContext.buttonGroup(
    disabled: ReactiveScope.() -> Boolean,
    buttons: List<ButtonGroupItem>,
) = run {
    forEach(
        direction = ForEachDirection.Horizontal,
        data = { buttons.reversed() },
        render = { index, item ->
            val actualIndex = buttons.size - index - 1
            val variant = when (actualIndex) {
                0 -> ButtonVariant.Contained
                1 -> ButtonVariant.Outlined
                else -> ButtonVariant.Text
            }
            button(
                options = ButtonOptions(
                    variant = variant,
                    palette = ButtonPalette.Primary,
                    size = ButtonSize.Medium,
                    fullWidth = false
                ),
                onClick = item.onClick,
                disabled = disabled,
            ) {
                text { content = item.text }
            } in padding(
                Insets(
                    left = if (variant == ButtonVariant.Text) 2.px else 4.px,
                    right = if (actualIndex == 0) 0.px else 4.px
                ),
            )
        }
    ) in padding(Insets.symmetric(vertical = 8.px))
}
