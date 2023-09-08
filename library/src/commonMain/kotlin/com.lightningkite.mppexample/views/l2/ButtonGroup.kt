package com.lightningkite.mppexample

data class ButtonGroupItem(
    val text: String,
    val onClick: suspend () -> Unit,
    val dangerous: Boolean = false
)

@ViewDsl
fun ViewContext.buttonGroup(
    disabled: ReactiveScope.() -> Boolean,
    disableAllWhenLoading: Boolean = true,
    buttons: List<ButtonGroupItem>,
) = run {
    val loading = if (disableAllWhenLoading) Property(false) else null

    forEachIndexed(
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
                    palette = if (item.dangerous) ButtonPalette.Danger else ButtonPalette.Primary,
                    size = ButtonSize.Medium,
                    fullWidth = false
                ),
                onClick = item.onClick,
                disabled = disabled,
                loading = loading,
            ) {
                text { content = item.text }
            } in padding(
                Insets(
                    left = if (variant == ButtonVariant.Text) 2.px else 4.px,
                    right = if (actualIndex == 0) 0.px else 4.px
                ),
            )
        }
    ) in padding(Insets.symmetric(vertical = 8.px)) in alignRight()
}

@ViewDsl
fun ViewContext.buttonGroup(
    buttons: List<ButtonGroupItem>,
) = buttonGroup(
    disabled = { false },
    buttons = buttons
)