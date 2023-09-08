package com.lightningkite.mppexample

@ViewDsl
fun ViewContext.toggleButton(
    toggled: ReactiveScope.() -> Boolean,
    onToggle: () -> Unit,
    disabled: ReactiveScope.() -> Boolean = { false },
    content: ReactiveScope.() -> String,
    corners: ReactiveScope.() -> CornerRadii = { CornerRadii(8.px) },
    icon: Icon = RockIcon.None,
) {
    val sizeMultiplier = 1.0

    nativeButton {
        onClick(onToggle)
        ::clickable { !disabled() }
        cursor = "pointer"
        icon(
            icon = { icon },
            width = 24.px,
            height = 24.px,
            color = { if (toggled()) theme.primary.foreground.closestColor() else theme.primary.background.closestColor() },
        ) in wrapIf(icon != RockIcon.None) { padding(right = 4.px) }
        text {
            ::content { content() }
            ::textStyle {
                TextStyle(
                    color = if (toggled()) theme.primary.foreground.closestColor() else theme.primary.background.closestColor(),
                    size = theme.baseSize
                )
            }
        }
    } in padding(
        Insets.symmetric(
            horizontal = (16 * sizeMultiplier).toInt().px,
            vertical = (8 * sizeMultiplier).toInt().px
        )
    ) in changingInteractive(
        background = {
            Background(
                fill = if (toggled()) theme.primary.background.closestColor() else theme.normal.background,
                stroke = theme.primary.background.closestColor(),
                strokeWidth = 1.px,
                corners = corners(),
            )
        },
        hoverBackground = {
            Background(
                fill = if (toggled()) theme.primary.background.closestColor().darken(0.1f) else theme.normal.background,
                stroke = theme.primary.background.closestColor().darken(0.1f),
                strokeWidth = 1.px
            )
        },
        downBackground = {
            Background(
                fill = if (toggled()) theme.primary.background.closestColor().darken(0.2f) else theme.normal.background,
                stroke = theme.primary.background.closestColor().darken(0.3f),
                strokeWidth = 1.px
            )
        },
        disabledBackground = {
            Background(
                fill = theme.normalDisabled?.background,
                stroke = theme.normalDisabled?.foreground?.closestColor(),
                strokeWidth = 1.px
            )
        },
    )
}
