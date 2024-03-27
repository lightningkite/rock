package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.contains
import com.lightningkite.kiteui.models.Icon
import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.reactive.await
import com.lightningkite.kiteui.reactive.invoke
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.l2.icon
import com.lightningkite.kiteui.views.visible

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCheckbox = FrameLayoutToggleButton

@ViewDsl
actual inline fun ViewWriter.checkboxActual(crossinline setup: Checkbox.() -> Unit): Unit {
    transitionNextView = ViewWriter.TransitionNextView.Yes
    toggleButton {
        icon(Icon.done, "") {
            ::visible.invoke { checked.await() }
        } 
        setup(Checkbox(this.native))
    }
}

actual inline var Checkbox.enabled: Boolean
    get() = native.enabled
    set(value) { native.enabled = value }
actual val Checkbox.checked: Writable<Boolean> get() =  native.checkedWritable