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
        } in margin(right = 4.px)
        row {
            gravity = RowGravity.Center
            setup()
        } in clickable(enabled = enabled) { checked.modify { !it } }
    }
}

@ViewDsl
fun ViewContext.checkBox(
    checked: Writable<Boolean>,
    disabled: Boolean = false,
    setup: NView.() -> Unit,
) = checkBox(checked = checked, disabled = { disabled }, setup = setup)
