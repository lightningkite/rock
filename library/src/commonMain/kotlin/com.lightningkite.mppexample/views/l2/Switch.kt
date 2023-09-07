package com.lightningkite.mppexample

@ViewDsl
fun ViewContext.switch(
    checked: Writable<Boolean>,
    setup: NView.() -> Unit
) {
    row {
        gravity = RowGravity.Center
        nativeSwitch {
            bind(checked)
            checkedColor = theme.primary.background.closestColor()
            checkedForegroundColor = theme.primary.foreground.closestColor()
        } in margin(right = 4.px)
        row {
            gravity = RowGravity.Center
            setup()
        } in clickable { checked.modify { !it } }
    }
}
