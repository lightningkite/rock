package com.lightningkite.mppexample

@ViewDsl
fun ViewContext.checkBox(
    checked: Writable<Boolean>,
    disabled: ReactiveScope.() -> Boolean = { false },
    setup: NView.() -> Unit
) {
    val enabled = SharedReadable { !disabled() }
    row {
        gravity = RowGravity.Center
        nativeCheckBox {
            bind(checked)
            ::disabled { disabled() }
            checkedColor = theme.primary.background.closestColor()
            checkedForegroundColor = theme.primary.foreground.closestColor()
        } in margin(right = 6.px)
        row {
            gravity = RowGravity.Center
            setup()
        } in clickable(enabled = enabled) { checked.modify { !it } }
    }
}

@ViewDsl
fun ViewContext.checkBox(
    checked: Writable<Boolean>,
    setup: NView.() -> Unit,
) = checkBox(checked = checked, disabled = { false }, setup = setup)
