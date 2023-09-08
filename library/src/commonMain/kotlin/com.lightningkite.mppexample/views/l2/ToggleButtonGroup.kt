package com.lightningkite.mppexample

data class ToggleButton<T>(
    val content: String,
    val value: T,
    val icon: Icon = RockIcon.None,
)

@ViewDsl
fun <T> ViewContext.toggleButtonGroup(
    toggled: ReactiveScope.() -> T,
    onToggle: (T) -> Unit,
    disabled: ReactiveScope.() -> Boolean = { false },
    buttons: ReactiveScope.() -> List<ToggleButton<T>>,
) {
    row {
        gravity = RowGravity.Center
        forEachIndexed(
            data = buttons,
            render = { index, button ->
                toggleButton(
                    toggled = { toggled() == button.value },
                    onToggle = { onToggle(button.value) },
                    disabled = disabled,
                    content = { button.content },
                    icon = button.icon,
                    corners = {
                        when (index) {
                            0 -> CornerRadii(topLeft = 8.px, bottomLeft = 8.px)
                            buttons().size - 1 -> CornerRadii(topRight = 8.px, bottomRight = 8.px)
                            else -> CornerRadii(0.px)
                        }
                    }
                )
            }
        )
    } in margin(vertical = 8.px)
}
