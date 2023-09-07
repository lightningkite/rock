package com.lightningkite.mppexample

@ViewDsl
fun ViewContext.switch(
    checked: Writable<Boolean>,
    disabled: ReactiveScope.() -> Boolean = { false },
    setup: NView.() -> Unit,
) {
    val enabled = SharedReadable { !disabled() }
    row {
        gravity = RowGravity.Center
        nativeSwitch {
            bind(checked)
            checkedColor = theme.primary.background.closestColor()
            checkedForegroundColor = theme.primary.foreground.closestColor()
            ::switchDisabled { disabled() }
        } in margin(right = 4.px)
        row {
            gravity = RowGravity.Center
            setup()
        } in clickable(enabled) { checked.modify { !it } }
    }
}
